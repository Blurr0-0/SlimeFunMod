package net.blurr.slimefun.item;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SeismicAxeItem extends Item {
    private static final float STRENGTH = 1.2F;
    private static final float HEIGHT = 0.9F;
    private static final float DAMAGE = 6;
    private static final float MIN_PLAYER_DISTANCE = 0.2F;
    private static final float MAX_GROUND_DISTANCE = 1.5F;
    private static final int RANGE = 10;

    public SeismicAxeItem(Properties properties) {
        super(properties.defaultDurability(250)); // Example durability setting
    }

//    @Override
//    public InteractionResult useOn(UseOnContext pContext) {
//        Level pLevel = pContext.getLevel();
//        Player pPlayer = pContext.getPlayer();
//        InteractionHand pUsedHand = pContext.getHand();
//        BlockPos pPos = pContext.getClickedPos();
//        List<Block> blocks = getBlocksInLineOfSight(pPlayer, RANGE);
//        Set<UUID> pushedEntities = new HashSet<>();
////        if (!pLevel.isClientSide()) { // Execute only on the server side
////            ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
////            createJumpingBlock(pLevel, pPos, pLevel.getBlockState(pPos), 10);
////            itemStack.hurt(4, new Random(), (ServerPlayer) pPlayer);
////            return InteractionResult.SUCCESS;
////        }
//        return InteractionResult.PASS;
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        List<BlockPos> blocks = getBlockPosesInLineOfSight(pPlayer, RANGE);
        Set<UUID> pushedEntities = new HashSet<>();

        // Skip the first two, too close to the player.
        for (int i = 2; i < blocks.size(); i++) {
            BlockState groundState = findGround(pLevel, blocks.get(i));
            BlockPos groundPos = blocks.get(i);

            pLevel.playSound(null, groundPos, groundState.getSoundType().getStepSound(), SoundSource.BLOCKS, 1.0F, 1.0F);

            // Check if they have room above.
            BlockPos blockAbovePos = groundPos.above();
            BlockState blockAboveState = pLevel.getBlockState(blockAbovePos);

            if (blockAboveState.isAir()) {
                createJumpingBlock(pLevel, groundPos, groundState, i);
            }

            Vec3 groundLocationVec = Vec3.atCenterOf(groundPos);
            pLevel.getEntitiesOfClass(Entity.class, new AABB(groundPos).inflate(5)).forEach(entity -> {
                if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)
                        && !entity.getUUID().equals(pPlayer.getUUID())
                        && canReach(pLevel, pPlayer, entity)
                        && pushedEntities.add(entity.getUUID())) {
                    pushEntity(pPlayer, entity);
                }
            });
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private void createJumpingBlock(Level pLevel, BlockPos groundPos, BlockState groundState, int index) {
        // Ensure you are on the server side before spawning entities
        if (!pLevel.isClientSide) {
            BlockPos posAbove = groundPos;
            double motionY = 0.4 + index * 0.01;
            FallingBlockEntity fallingBlockEntity = createFallingBlock(pLevel, groundState, posAbove, new Vec3(0, motionY, 0), false, 580);
            fallingBlockEntity.setPos(posAbove.getX() + 0.5, posAbove.getY(), posAbove.getZ() + 0.5);
            fallingBlockEntity.setStartPos(fallingBlockEntity.blockPosition());
            pLevel.addFreshEntity(fallingBlockEntity);
        }
    }

    public FallingBlockEntity createFallingBlock(Level pLevel, BlockState pState, BlockPos pPos, Vec3 Motion, boolean dropItem, int time) {
        FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, pLevel);
        try {
            Field blockStateField = FallingBlockEntity.class.getDeclaredField("blockState");
            blockStateField.setAccessible(true); // Bypass the access check
            blockStateField.set(fallingBlockEntity, pState); // Set the desired state
            fallingBlockEntity.blocksBuilding = true;
            fallingBlockEntity.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
            fallingBlockEntity.setDeltaMovement(Motion);
            fallingBlockEntity.xo = pPos.getX();
            fallingBlockEntity.yo = pPos.getY();
            fallingBlockEntity.zo = pPos.getZ();
            fallingBlockEntity.setStartPos(fallingBlockEntity.blockPosition());
            fallingBlockEntity.dropItem = dropItem;
            fallingBlockEntity.time = time;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fallingBlockEntity;
    }

    private void pushEntity(Player pPLayer, Entity pEntity) {
        Level pLevel = pPLayer.getLevel();

        if (!(pEntity instanceof Player) /*|| pLevel.getGameRules().getBoolean(GameRules.) */) {
            Vec3 direction = pEntity.getPosition(1f).subtract(pPLayer.getPosition(1f)).normalize();
            direction = direction.multiply(STRENGTH, HEIGHT, STRENGTH); // 방향 및 강도 조정

            pEntity.lerpMotion(direction.x, direction.y, direction.z);

            pEntity.hurt(DamageSource.playerAttack(pPLayer), (float) DAMAGE);
        }
    }

    private boolean canReach(Level pLevel, Entity pPlayer, Entity pEntity) {
        double maxGroundDistanceSquared = MAX_GROUND_DISTANCE * MAX_GROUND_DISTANCE;
        double minPlayerDistanceSquared = MIN_PLAYER_DISTANCE * MIN_PLAYER_DISTANCE;

        BlockPos playerPos = pPlayer.getOnPos();
        BlockPos entityPos = pEntity.getOnPos();
        BlockPos groundPos = entityPos.below();
        double groundDistanceSquared = Vec3.atCenterOf(groundPos).subtract(Vec3.atCenterOf(entityPos)).lengthSqr();
        double playerDistanceSquared = Vec3.atCenterOf(playerPos).subtract(Vec3.atCenterOf(entityPos)).lengthSqr();

        return groundDistanceSquared < maxGroundDistanceSquared && playerDistanceSquared > minPlayerDistanceSquared;
    }

    public List<Block> getBlocksInLineOfSight(Player pPLayer, int range) {
        List<Block> blocks = new ArrayList<>();
        Level pLevel = pPLayer.getLevel();

        Vec3 startVec = pPLayer.getEyePosition(1.0F);
        Vec3 lookVec = pPLayer.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, pPLayer);
        BlockHitResult result = pLevel.clip(context);

        if (result.getType() == BlockHitResult.Type.BLOCK) {
            BlockPos blockPos = result.getBlockPos();
            BlockPos.betweenClosed(blockPos.offset(-1, -1, -1), blockPos.offset(1, 1, 1)).forEach(pos -> {
                BlockState state = pLevel.getBlockState(pos);
                Block block = state.getBlock();
                if (!blocks.contains(block)) {
                    blocks.add(block);
                }
            });
        }

        return blocks;
    }

    public List<BlockPos> getBlockPosesInLineOfSight(Player player, int range) {
        List<BlockPos> blockPositions = new ArrayList<>();
        Level level = player.getLevel();

        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F).normalize(); // Normalize the direction vector for consistent steps

        // Loop through each point in the line of sight up to the specified range
        for (int i = 0; i <= range; i++) {
            Vec3 currentVec = startVec.add(lookVec.x * i, lookVec.y * i, lookVec.z * i);
            BlockPos currentPos = new BlockPos(currentVec);

            // Add the current position if it's not already in the list
            if (!blockPositions.contains(currentPos)) {
                blockPositions.add(currentPos);
                // Optionally, check the block type here with: level.getBlockState(currentPos)
            }
        }

        return blockPositions;
    }

    private @Nonnull BlockState findGround(@Nonnull Level world, @Nonnull BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            int minHeight = world.getMinBuildHeight();
            for (int y = pos.getY() - 1; y >= minHeight; y--) {
                BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
                BlockState currentState = world.getBlockState(currentPos);

                if (!currentState.isAir()) {
                    return currentState;
                }
            }
        }

        return state;
    }

//    private void activateAbility(Level pLevel, Player pPlayer) {
//        List<BlockPos> blocks = getBlocksInRange(pPlayer, RANGE);
//        Set<UUID> pushedEntities = new HashSet<>();
//
//        for (int i = 2; i < blocks.size(); i++) {
//            BlockPos groundPos = findGround(pLevel, blocks.get(i));
//            BlockState groundState = pLevel.getBlockState(groundPos);
////            play break effect
////            pLevel.playEvent(2001, groundPos, Block.getStateId(groundState));
//
//            BlockPos blockAbovePos = groundPos.above();
//
//            if (pLevel.getBlockState(blockAbovePos).isAir()) {
//                createJumpingBlock(pLevel, groundPos, blockAbovePos, i);
//            }
//
//            for (Entity entity : pLevel.getEntities(pPlayer, new AABB(groundPos).inflate(1.5))) {
//                if (entity.getUUID() != pPlayer.getUUID() && pushedEntities.add(entity.getUUID())) {
//                    pushEntity(pPlayer, entity);
//                }
//            }
//        }
//    }
//
}