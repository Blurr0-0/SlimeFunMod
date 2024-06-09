package net.blurr.slimefun.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.blurr.slimefun.block.entity.BlockPlacerEntity;
import net.blurr.slimefun.block.entity.OreWasherEntity;
import net.blurr.slimefun.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class BlockPlacer extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final Map<Item, DispenseItemBehavior> PLACER_REGISTRY = Util.make(new Object2ObjectOpenHashMap<>(), (p_52723_) -> {
        p_52723_.defaultReturnValue(new DefaultDispenseItemBehavior());
    });

    private static final int TRIGGER_DURATION = 4;

    public static void registerBehavior(ItemLike pItem, DispenseItemBehavior pBehavior) {
        PLACER_REGISTRY.put(pItem.asItem(), pBehavior);
    }

    public BlockPlacer(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, Boolean.valueOf(false)));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity pBlockEntity = pLevel.getBlockEntity(pPos);

        if (!pLevel.isClientSide()) {
            if(pBlockEntity instanceof BlockPlacerEntity) {
                NetworkHooks.openGui(((ServerPlayer)pPlayer), (BlockPlacerEntity)pBlockEntity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }


        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    //todo add the ability to place blocks with storage like shulker boxes without the items inside disappearing

    protected void dispenseFrom(ServerLevel pLevel, BlockPos pPos) {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(pLevel, pPos);
        BlockPlacerEntity BlockPlacerEntity = blocksourceimpl.getEntity();
        BlockState pState = pLevel.getBlockState(pPos);
        int i = BlockPlacerEntity.getRandomSlot();
        if (i < 0) {
            pLevel.levelEvent(1001, pPos, 0);
            pLevel.gameEvent(GameEvent.DISPENSE_FAIL, pPos);
        } else {
            ItemStack itemstack = BlockPlacerEntity.getItem(i);
            BlockPos relativePos = pPos.relative(pState.getValue(FACING).getOpposite().getOpposite());
            if (itemstack.getItem() instanceof BlockItem blockItem && (pLevel.getBlockState(relativePos).isAir() || (pLevel.getBlockState(relativePos).getMaterial().isReplaceable() && pLevel.getBlockState(relativePos).getBlock() != blockItem.getBlock()))) {
                pLevel.setBlock(relativePos, blockItem.getBlock().defaultBlockState(), 3);
                itemstack.shrink(1);
                BlockPlacerEntity.setItem(i, itemstack);
            }
//            DispenseItemBehavior dispenseitembehavior = this.getDispenseMethod(itemstack);
//            if (dispenseitembehavior != DispenseItemBehavior.NOOP) {
//                BlockPlacerEntity.setItem(i, dispenseitembehavior.dispense(blocksourceimpl, itemstack));
//            }

        }
    }

    protected DispenseItemBehavior getDispenseMethod(ItemStack pStack) {
        return PLACER_REGISTRY.get(pStack.getItem());
    }
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean flag = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean flag1 = pState.getValue(TRIGGERED);
        if (flag && !flag1) {
            pLevel.scheduleTick(pPos, this, 4);
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!flag && flag1) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.valueOf(false)), 4);
        }

    }
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        this.dispenseFrom(pLevel, pPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockPlacerEntity(pPos, pState);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BlockPlacerEntity) {
                ((BlockPlacerEntity)blockentity).setCustomName(pStack.getHoverName());
            }
        }

    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BlockPlacerEntity) {
                Containers.dropContents(pLevel, pPos, (BlockPlacerEntity)blockentity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public static Position getDispensePosition(BlockSource pBlockSource) {
        Direction direction = pBlockSource.getBlockState().getValue(FACING);
        double d0 = pBlockSource.x() + 0.7D * (double)direction.getStepX();
        double d1 = pBlockSource.y() + 0.7D * (double)direction.getStepY();
        double d2 = pBlockSource.z() + 0.7D * (double)direction.getStepZ();
        return new PositionImpl(d0, d1, d2);
    }
    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }
    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TRIGGERED);
    }
}
