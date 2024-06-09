package net.blurr.slimefun.block;

import net.blurr.slimefun.block.entity.AbstractEnhancedFurnaceBlockEntity;
import net.blurr.slimefun.block.entity.EnhancedFurnaceEntity;
import net.blurr.slimefun.block.entity.ModBlockEntities;
import net.blurr.slimefun.block.entity.SmelteryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class EnhancedFurnace extends AbstractFurnaceBlock {

    int speed;
    int efficiency;
    int luck;

    public EnhancedFurnace(int processingSpeed, int fuelEfficiency, int luckMultiplier, BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.speed = processingSpeed;
        this.efficiency = fuelEfficiency;
        this.luck = luckMultiplier;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EnhancedFurnaceEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createEnhancedFurnaceTicker(pLevel, pBlockEntityType, ModBlockEntities.BLOCK_ENHANCED_FURNACE_ENTITY.get());
    }

    /**
     * Called to open this furnace's container.
     *
     * @see #use
     */
    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof EnhancedFurnaceEntity) {
            pPlayer.openMenu((MenuProvider)blockentity);
            pPlayer.awardStat(Stats.INTERACT_WITH_FURNACE);
        }

    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        BlockEntity pBlockEntity = pLevel.getBlockEntity(pPos);
        if (pBlockEntity instanceof EnhancedFurnaceEntity enhancedFurnace) {
            enhancedFurnace.setProcessingSpeed(speed);
            enhancedFurnace.setFuelEfficiency(efficiency);
            enhancedFurnace.setLuckMultiplier(luck);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof EnhancedFurnaceEntity) {
                ((EnhancedFurnaceEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if (pState.getValue(LIT)) {
            double d0 = (double)pPos.getX() + 0.5D;
            double d1 = (double)pPos.getY();
            double d2 = (double)pPos.getZ() + 0.5D;
            if (pRand.nextDouble() < 0.1D) {
                pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = pState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = pRand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
            double d6 = pRand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
            pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    protected static <T extends BlockEntity> BlockEntityTicker<T> createEnhancedFurnaceTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends AbstractEnhancedFurnaceBlockEntity> pClientType) {
        return pLevel.isClientSide ? null : createTickerHelper(pServerType, pClientType, AbstractEnhancedFurnaceBlockEntity::serverTick);
    }
}
