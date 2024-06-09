package net.blurr.slimefun.block;

import net.blurr.slimefun.block.entity.ModBlockEntities;
import net.blurr.slimefun.block.entity.SmelteryEntity;
import net.blurr.slimefun.block.state.properties.Lit;
import net.blurr.slimefun.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Smeltery extends BaseEntityBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Lit> LIT = EnumProperty.create("lit", Lit.class);

    protected static final VoxelShape VOXEL_SHAPE = Shapes.or(Block.box(1.0D, 10.0D, 1.0D, 15.0D, 13.0D, 15.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D));

    protected Smeltery(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH).setValue(LIT, Lit.OFF));
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

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(LIT) == Lit.FIRE) {
            return 15;
        } else if (state.getValue(LIT) == Lit.SOULFIRE) {
            return 10;
        }
        return 0;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).isAir();
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, FACING, LIT);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.SMELTERY_ENTITY.get(),
                SmelteryEntity::tick);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(FACING, pState.getValue(FACING)), 3);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            if (pPlayer.isCreative()) {
                breakSmeltery(pLevel, pPos, pState, pPlayer);
            } else {
                dropResources(pState, pLevel, pPos, (BlockEntity)null, pPlayer, pPlayer.getMainHandItem());
                breakSmeltery(pLevel, pPos, pState, pPlayer);
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    protected static void breakSmeltery(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pPos.below();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = Blocks.AIR.defaultBlockState();pLevel.setBlock(pPos, blockstate1, 35);
                pLevel.setBlock(blockpos, blockstate1, 35);
                pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
            }
        }
        if (doubleblockhalf == DoubleBlockHalf.LOWER) {
            BlockPos blockpos = pPos.above();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.UPPER) {
                BlockState blockstate1 = Blocks.AIR.defaultBlockState();pLevel.setBlock(pPos, blockstate1, 35);
                pLevel.setBlock(blockpos, blockstate1, 35);
                pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRandom) {
        if (pState.getValue(LIT) == Lit.FIRE && pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound((double)pPos.below(1).getX() + 0.5D, (double)pPos.below(1).getY() + 0.5D, (double)pPos.below(1).getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
            }

            if (pRandom.nextInt(5) == 0) {
                for(int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                    pLevel.addParticle(ParticleTypes.LAVA, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, (double)(pRandom.nextFloat() / 2.0F), 5.0E-5D, (double)(pRandom.nextFloat() / 2.0F));
                }
            }
//            if (pRandom.nextFloat() < 0.9F) {
                for(int i = 0; i < pRandom.nextInt(2) + 2; ++i) {
                    CampfireBlock.makeParticles(pLevel, new BlockPos(pPos.getX(), pPos.getY() + 0.5D, pPos.getZ()), false, false);
                }
//            }

        }
        if (pState.getValue(LIT) == Lit.SOULFIRE && pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound((double)pPos.below(1).getX() + 0.5D, (double)pPos.below(1).getY() + 0.5D, (double)pPos.below(1).getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
            }

            if (pRandom.nextInt(5) == 0) {
                for(int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                    pLevel.addParticle(ModParticles.SOUL_LAVA.get(), (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, (double)(pRandom.nextFloat() / 2.0F), 5.0E-5D, (double)(pRandom.nextFloat() / 2.0F));
                }
            }
//            if (pRandom.nextFloat() < 0.9F) {
            for(int i = 0; i < pRandom.nextInt(2) + 2; ++i) {
                CampfireBlock.makeParticles(pLevel, new BlockPos(pPos.getX(), pPos.getY() + 0.5D, pPos.getZ()), false, false);
            }
//            }

        }
    }
}
