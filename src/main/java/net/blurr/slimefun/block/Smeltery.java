package net.blurr.slimefun.block;

import net.blurr.slimefun.block.entity.ModBlockEntities;
import net.blurr.slimefun.block.entity.SmelteryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class Smeltery extends BaseEntityBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape VOXEL_SHAPE = Shapes.or(Block.box(1.0D, 10.0D, 1.0D, 15.0D, 13.0D, 15.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D));

    protected Smeltery(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return new SmelteryEntity(pPos, pState);
        } else {
            return null;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return Shapes.block();
        } else {
            return VOXEL_SHAPE;
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SmelteryEntity) {
                ((SmelteryEntity) blockEntity).drops();
            }
        }
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER && pLevel.getBlockState(pPos.above()).getBlock() == ModBlocks.SMELTERY.get()) {
            pLevel.destroyBlock(pPos.above(), false);
        }
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER && pLevel.getBlockState(pPos.below()).getBlock() == ModBlocks.SMELTERY.get()) {
            pLevel.destroyBlock(pPos.below(), false);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockPos entityPose;
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            entityPose = pPos;
        } else {
            entityPose = pPos.below();
        }
        BlockEntity pBlockEntity = pLevel.getBlockEntity(entityPose);
        if (!pLevel.isClientSide()) {
            if(pBlockEntity instanceof SmelteryEntity) {
                NetworkHooks.openGui(((ServerPlayer)pPlayer), (SmelteryEntity)pBlockEntity, entityPose);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }


        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).isAir();
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.SMELTERY_ENTITY.get(),
                SmelteryEntity::tick);
    }
}
