package net.blurr.slimefun.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.item.ModItems;
import net.blurr.slimefun.util.EnchantmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//todo simplify code or just make it easier on my eyeballs

@Mod.EventBusSubscriber(modid = SlimeFun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEvents {

    //lumber axe
    @SubscribeEvent
    public static void LumberAxeBreak(BlockEvent.BreakEvent event) {
        BlockPos pStartPos = event.getPos();
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();

        Queue<BlockPos> logsToBreak = findLogsToBreak(pStartPos, pPlayer, pLevel);
        for (BlockPos pos : logsToBreak) {
            if (!pPlayer.isCreative()) {
                pLevel.destroyBlock(pos, true, pPlayer);
                pPlayer.getMainHandItem().hurt(1, new Random(), (ServerPlayer) pPlayer);

            } else {
                pLevel.destroyBlock(pos, false, pPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void LumberAxeOutline(RenderLevelStageEvent event) {
        Level level = Minecraft.getInstance().level;
        HitResult hitresult = Minecraft.getInstance().hitResult;
        if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos targetPos = ((BlockHitResult) hitresult).getBlockPos();
            BlockState state = level.getBlockState(targetPos);
            Entity entity = event.getCamera().getEntity();
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS && !state.isAir() && entity instanceof Player player && player.isHolding(ModItems.LUMBER_AXE.get()) && state.is(BlockTags.LOGS)) {
                PoseStack stack = event.getPoseStack();
                stack.pushPose();
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                Vec3 cameraPos = event.getCamera().getPosition();
                stack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());

                Queue<BlockPos> logsToBreak = findLogsToBreak(targetPos, player, level);
                VoxelShape combinedShape = Shapes.empty();
                for (BlockPos pos : logsToBreak) {
                    BlockState logState = level.getBlockState(pos);
                    VoxelShape shape = logState.getShape(level, pos, CollisionContext.of(entity)).move(pos.getX(), pos.getY(), pos.getZ());
                    combinedShape = Shapes.or(combinedShape, shape);
                }
                renderShape(stack, vertexConsumer, combinedShape, 0, 0, 0, 1.0F, 1.0F, 1.0F, 0.4F);

                bufferSource.endBatch(RenderType.lines());
                stack.popPose();
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHighlight(DrawSelectionEvent.HighlightBlock event) {
        BlockState state = event.getCamera().getEntity().level.getBlockState(event.getTarget().getBlockPos());
        if (event.getCamera().getEntity() instanceof Player player && player.isHolding(ModItems.LUMBER_AXE.get()) && state.is(BlockTags.LOGS) && !player.isCrouching()) {
            event.setCanceled(true);
        }
    }

    public static Queue<BlockPos> findLogsToBreak(BlockPos startPos, Player player, Level level) {
        Queue<BlockPos> positionsToCheck = new LinkedList<>();
        Queue<BlockPos> logsToBreakQueue = new LinkedList<>();
        Set<BlockPos> visitedPositions = new HashSet<>();
        positionsToCheck.add(startPos);

        int logsBroken = 0;

        while (!positionsToCheck.isEmpty() && logsBroken < 100 && player.isHolding(ModItems.LUMBER_AXE.get()) && !player.isCrouching()) {
            BlockPos currentPosition = positionsToCheck.poll();
            if (visitedPositions.contains(currentPosition)) {
                continue;
            }

            visitedPositions.add(currentPosition);

            if (level.getBlockState(currentPosition).is(BlockTags.LOGS)) {
                logsToBreakQueue.add(currentPosition);
                logsBroken++;

                positionsToCheck.add(currentPosition.above());
                positionsToCheck.add(currentPosition.below());
                positionsToCheck.add(currentPosition.north());
                positionsToCheck.add(currentPosition.south());
                positionsToCheck.add(currentPosition.west());
                positionsToCheck.add(currentPosition.east());
            }
        }

        return logsToBreakQueue;
    }

    private static void renderShape(PoseStack p_109783_, VertexConsumer p_109784_, VoxelShape p_109785_, double p_109786_, double p_109787_, double p_109788_, float p_109789_, float p_109790_, float p_109791_, float p_109792_) {
        PoseStack.Pose posestack$pose = p_109783_.last();
        p_109785_.forAllEdges((p_194324_, p_194325_, p_194326_, p_194327_, p_194328_, p_194329_) -> {
            float f = (float)(p_194327_ - p_194324_);
            float f1 = (float)(p_194328_ - p_194325_);
            float f2 = (float)(p_194329_ - p_194326_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3;
            f1 /= f3;
            f2 /= f3;
            p_109784_.vertex(posestack$pose.pose(), (float)(p_194324_ + p_109786_), (float)(p_194325_ + p_109787_), (float)(p_194326_ + p_109788_)).color(p_109789_, p_109790_, p_109791_, p_109792_).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            p_109784_.vertex(posestack$pose.pose(), (float)(p_194327_ + p_109786_), (float)(p_194328_ + p_109787_), (float)(p_194329_ + p_109788_)).color(p_109789_, p_109790_, p_109791_, p_109792_).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }

    //smelting pickaxe
    @SubscribeEvent
    public static void SmelterPickaxeBreak(BlockEvent.BreakEvent event) {
        BlockPos pPos = event.getPos();
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        BlockState pState = event.getState();

        if (!pPlayer.isCreative() && pPlayer.isHolding(ModItems.SMELTERS_PICKAXE.get())) {
            List<ItemStack> drops = Block.getDrops(pState, (ServerLevel) pLevel, pPos, pLevel.getBlockEntity(pPos));

            for (int i = 0; i < drops.size(); i++) {
                ItemStack pStack = drops.get(i);
                ItemStack smeltedStack = smelt(pStack, pLevel);
                ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, smeltedStack);
                pLevel.addFreshEntity(itemEntity);
                drops.set(i, smeltedStack); // Replace the original stack with the smelted stack
            }


            pLevel.destroyBlock(pPos, false, pPlayer);

        }

    }

    private static ItemStack smelt(ItemStack stack, Level level) {
        SmeltingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), level).orElse(null);
        if (recipe != null) {
            ItemStack result = recipe.getResultItem().copy();
            result.setCount(stack.getCount() * result.getCount());
            return result;
        }
        return stack;
    }

    //Talismans
    //todo add tellraw that tells when talisman is used, add some particles + sounds to indicate usage too, add ender version support
    @SubscribeEvent
    public static void AnvilTalisman(PlayerDestroyItemEvent event) {
        Player pPlayer = event.getPlayer();
        ItemStack pStack = event.getOriginal();
        InteractionHand pHand = event.getHand();
        if (pStack.isDamageableItem()) {
            Slot slot = playerHasItem(pPlayer, ModItems.ANVIL_TALISMAN.get().asItem());
            if (slot != null) {
                slot.getItem().shrink(1);
                pStack.setDamageValue(0);
                pPlayer.setItemInHand(pHand, pStack);
            }
        }
    }

    @SubscribeEvent
    public static void MinerTalisman(BlockEvent.BreakEvent event) {
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        if (pLevel.getRandom().nextInt(100) < 20) {
            BlockPos pPos = event.getPos();
            BlockState pState = pLevel.getBlockState(pPos);
            Slot slot = playerHasItem(pPlayer, ModItems.MINER_TALISMAN.get().asItem());
            if (slot != null && pState.is(Tags.Blocks.ORES) && !pPlayer.isCreative()) {
                List<ItemStack> drops = Block.getDrops(pState, (ServerLevel) pLevel, pPos, pLevel.getBlockEntity(pPos));

                for (int i = 0; i < drops.size(); i++) {
                    ItemStack pStack = drops.get(i);
                    pStack.grow(2*pStack.getCount());
                    ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, pStack);
                    pLevel.addFreshEntity(itemEntity);
                    drops.set(i, pStack);
                }

                pLevel.destroyBlock(pPos, false, pPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void FarmerTalisman(BlockEvent.BreakEvent event) {
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        if (pLevel.getRandom().nextInt(100) < 20) {
            BlockPos pPos = event.getPos();
            BlockState pState = pLevel.getBlockState(pPos);
            Slot slot = playerHasItem(pPlayer, ModItems.FARMER_TALISMAN.get().asItem());
            if (slot != null && pState.getBlock() instanceof CropBlock && !pPlayer.isCreative()) {
                List<ItemStack> drops = Block.getDrops(pState, (ServerLevel) pLevel, pPos, pLevel.getBlockEntity(pPos));

                for (int i = 0; i < drops.size(); i++) {
                    ItemStack pStack = drops.get(i);
                    pStack.grow(2*pStack.getCount());
                    ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, pStack);
                    pLevel.addFreshEntity(itemEntity);
                    drops.set(i, pStack);
                }

                pLevel.destroyBlock(pPos, false, pPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void HunterTalisman(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player pPlayer && !(event.getEntityLiving() instanceof Player)) {
            LivingEntity pEntity = event.getEntityLiving();
            Level pLevel = pPlayer.getLevel();
            if (pLevel.getRandom().nextInt(100) < 20) {
                BlockPos pPos = pEntity.getOnPos().above();
                Slot slot = playerHasItem(pPlayer, ModItems.HUNTER_TALISMAN.get().asItem());
                if (slot != null) {
                    List<ItemEntity> drops = new ArrayList<>(event.getDrops());
                    for (int i = 0; i < drops.size(); i++) {
                        ItemStack pStack = drops.get(i).getItem();
                        pStack.grow(2*pStack.getCount());
                        ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, pStack);
                        pLevel.addFreshEntity(itemEntity);
                        drops.set(i, itemEntity);
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void LavaWalkerTalisman(TickEvent.PlayerTickEvent event) {
        Player pPlayer = event.player;
        if (pPlayer.isInLava() && !pPlayer.isCreative() && !pPlayer.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            Slot slot = playerHasItem(pPlayer, ModItems.LAVA_WALKER_TALISMAN.get());
            if (slot != null) {
                slot.getItem().shrink(1);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3 * 60 * 20));
            }
        }
    }

    @SubscribeEvent
    public static void WaterBreatherTalisman(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player pPlayer && event.getSource() == DamageSource.DROWN) {
            Slot slot = playerHasItem(pPlayer, ModItems.WATER_BREATHER_TALISMAN.get());
            if (slot != null) {
                slot.getItem().shrink(1);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 3 * 60 * 20));
            }
        }
    }

    @SubscribeEvent
    public static void AngelTalisman(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player pPlayer && event.getSource() == DamageSource.FALL) {
            Slot slot = playerHasItem(pPlayer, ModItems.ANGEL_TALISMAN.get());
            if (slot != null && pPlayer.level.random.nextInt(100) < 75) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void FirefighterTalisman(TickEvent.PlayerTickEvent event) {
        Player pPlayer = event.player;
        if (pPlayer.isOnFire() && !pPlayer.isCreative() && !pPlayer.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            Slot slot = playerHasItem(pPlayer, ModItems.FIREFIGHTER_TALISMAN.get());
            if (slot != null) {
                slot.getItem().shrink(1);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3 * 60 * 20));
            }
        }
    }

    @SubscribeEvent
    public static void MagicianTalisman(ModEvents.PlayerEnchantEvent event) {
        Player pPlayer = event.getPlayer();
        ItemStack pStack = event.getItemStack();
        int pSeed = event.getSeed();
        Slot slot = playerHasItem(pPlayer, ModItems.MAGICIAN_TALISMAN.get());
        if (slot != null && pPlayer.level.getRandom().nextInt(100) < 80) {
            Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(pStack);
            Set<Enchantment> existingEnchantmentSet = existingEnchantments.keySet();

            List<Enchantment> possibleEnchantments = new ArrayList<>();
            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                if (enchantment.canEnchant(pStack) && !existingEnchantments.containsKey(enchantment)) {
                    if (EnchantmentUtil.canAddEnchantment(pStack, enchantment)) {
                        possibleEnchantments.add(enchantment);
                    }
                }
            }

            if (!possibleEnchantments.isEmpty()) {
                Enchantment randomEnchantment = possibleEnchantments.get(ThreadLocalRandom.current().nextInt(possibleEnchantments.size()));
                int level = new Random(pSeed).nextInt(1, randomEnchantment.getMaxLevel() + 1);

                pStack.enchant(randomEnchantment, level);
                pPlayer.displayClientMessage(new TextComponent("Your item has been enchanted with " + randomEnchantment.getFullname(level).getString() + "!"), true);
            }
        }
    }

    private static Map<UUID, Boolean> sprintKeyStates = new HashMap<>();

    @SubscribeEvent
    public static void TravellerTalisman(TickEvent.PlayerTickEvent event) {
        Player pPlayer = event.player;
        if (pPlayer.isSprinting() && !pPlayer.hasEffect(MobEffects.MOVEMENT_SPEED) && pPlayer.level.getRandom().nextInt(100) < 60 && getSprintKeyState(pPlayer)) {
            Slot slot = playerHasItem(pPlayer, ModItems.TRAVELLER_TALISMAN.get());
            if (slot != null) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20, 2));
            }
        }
        setSprintKeyState(pPlayer, false);
    }

    @SubscribeEvent
    public static void TravellerTalisman(InputEvent.KeyInputEvent event) {
        if (event.getKey() == GLFW.GLFW_KEY_LEFT_CONTROL) {
            setSprintKeyState(Minecraft.getInstance().player, event.getAction() == GLFW.GLFW_PRESS);
        }
    }

    @SubscribeEvent
    public static void WarriorTalisman(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player pPlayer && (event.getSource() instanceof IndirectEntityDamageSource || event.getSource() instanceof EntityDamageSource) && !pPlayer.hasEffect(MobEffects.DAMAGE_BOOST)) {
            Slot slot = playerHasItem(pPlayer, ModItems.WARRIOR_TALISMAN.get());
            if (slot != null) {
                slot.getItem().shrink(1);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3 * 60 * 20, 2));
            }
        }
    }

    @SubscribeEvent
    public static void KnightTalisman(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player pPlayer && (event.getSource() instanceof IndirectEntityDamageSource || event.getSource() instanceof EntityDamageSource) && !pPlayer.hasEffect(MobEffects.REGENERATION) && pPlayer.level.getRandom().nextInt(100) < 30) {
            Slot slot = playerHasItem(pPlayer, ModItems.KNIGHT_TALISMAN.get());
            if (slot != null) {
                slot.getItem().shrink(1);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5 * 20, 4));
            }
        }
    }

    @SubscribeEvent
    public static void CavemanTalisman(BlockEvent.BreakEvent event) {
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        if (pLevel.getRandom().nextInt(100) < 50) {
            BlockPos pPos = event.getPos();
            BlockState pState = pLevel.getBlockState(pPos);
            Slot slot = playerHasItem(pPlayer, ModItems.CAVEMAN_TALISMAN.get().asItem());
            if (slot != null && pState.is(Tags.Blocks.ORES) && !pPlayer.isCreative() && !pPlayer.hasEffect(MobEffects.DIG_SPEED)) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40 * 20, 2));
            }
        }
    }

    @SubscribeEvent
    public static void WiseTalisman(PlayerXpEvent.PickupXp event) {
        Player pPlayer = event.getPlayer();
        ExperienceOrb pOrb = event.getOrb();
        if (pPlayer.level.getRandom().nextInt(100) < 20) {
            Slot slot = playerHasItem(pPlayer, ModItems.WISE_TALISMAN.get().asItem());
            if (slot != null) {
                pOrb.value *= 2;
            }
        }
    }

    @SubscribeEvent
    public static void WhirlwindTalisman(ProjectileImpactEvent event) {
        Projectile pProjectile = event.getProjectile();
        if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Player pPlayer && !(pProjectile instanceof ThrownTrident || pProjectile instanceof ThrownPotion || pProjectile instanceof ThrownEnderpearl || pProjectile instanceof ThrownExperienceBottle) && pProjectile.getOwner() != pPlayer && pPlayer.level.getRandom().nextInt(100) < 60) {
            Slot slot = playerHasItem(pPlayer, ModItems.WHIRLWIND_TALISMAN.get());
            if (slot != null) {
                reflectProjectile(pProjectile, pPlayer);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void WizardTalisman(ModEvents.PlayerEnchantEvent event) {
        Player pPlayer = event.getPlayer();
        ItemStack pStack = event.getItemStack();
        int pSeed = event.getSeed();

        Slot slot = playerHasItem(pPlayer, ModItems.WIZARD_TALISMAN.get());
        if (slot != null && EnchantmentHelper.getEnchantments(pStack).containsKey(Enchantments.BLOCK_FORTUNE)) {
            Random pRandom = new Random(pSeed);
            int pFortune = pRandom.nextInt(2) + 4;
            pStack.enchant(Enchantments.BLOCK_FORTUNE, pFortune);
            Map<Enchantment, Integer> pEnchantments = EnchantmentHelper.getEnchantments(pStack);
            for (Map.Entry<Enchantment, Integer> pEntry : pEnchantments.entrySet()) {
                Enchantment pEnchantment = pEntry.getKey();
                int level = pEntry.getValue();

                if (pEnchantment == Enchantments.BLOCK_FORTUNE) continue;
                if (pRandom.nextBoolean()) {
                    int pLevel = Math.max(1, level - 1);
                    pEnchantments.put(pEnchantment, Math.max(1, level - 1));
                    pPlayer.displayClientMessage(new TextComponent("" + pEnchantment.getFullname(pLevel + 1).getString() + " was lowered!"), true);
                }
            }

            EnchantmentHelper.setEnchantments(pEnchantments, pStack);
        }
    }

    private static void reflectProjectile(Projectile pProjectile, LivingEntity pTraget) {
        Vec3 motion = pProjectile.getDeltaMovement().scale(-1);
        pProjectile.setDeltaMovement(motion);
        float pYaw = (float) Math.toDegrees(Math.atan2(-motion.x, motion.y));
        pProjectile.setYRot(pYaw);
        pProjectile.setOwner(pTraget);
    }

    public static Slot playerHasItem(Player pPlayer, Item pItem) {
        for (Slot slot : pPlayer.inventoryMenu.slots) {
            if (slot.getItem().getItem() == pItem) {
                return slot;
            }
        }
        return null;
    }

    private static void setSprintKeyState(Player player, boolean pressed) {
        sprintKeyStates.put(player.getUUID(), pressed);
    }

    private static boolean getSprintKeyState(Player player) {
        return sprintKeyStates.getOrDefault(player.getUUID(), false);
    }
}
