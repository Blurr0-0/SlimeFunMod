package net.blurr.slimefun.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class GoldPan extends Item {
    public GoldPan(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        Player pPlayer = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        Random rand = new Random();

        if (pState.getBlock() == Blocks.GRAVEL) {

            pLevel.levelEvent(2001, pPos, Block.getId(pState));
            if (!pLevel.isClientSide()) {

                if (pPlayer instanceof ServerPlayer) {

                    if (!pPlayer.isCreative()) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) pPlayer, pPos, pStack);
                        pStack.hurt(1, new Random(), (ServerPlayer) pPlayer);
                    }

                    pPlayer.getCooldowns().addCooldown(this, 5);
                }

                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);

                if (rand.nextInt(100) < 40) {
                    ItemStack flint = new ItemStack(Items.FLINT);
                    ItemEntity Flint = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), flint);
                    pLevel.addFreshEntity(Flint);

                } else if (rand.nextInt(100) < 35) {
                    ItemStack siftedOre = new ItemStack(ModItems.SIFTED_ORE.get());
                    ItemEntity SiftedOre = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), siftedOre);
                    pLevel.addFreshEntity(SiftedOre);

                } else if (rand.nextInt(100) < 20) {
                    ItemStack clay = new ItemStack(Items.CLAY_BALL);
                    ItemEntity Clay = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), clay);
                    pLevel.addFreshEntity(Clay);

                } else if (rand.nextInt(100) < 5){
                    ItemStack ironNugget = new ItemStack(Items.IRON_NUGGET);
                    ItemEntity IronNugget = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), ironNugget);
                    pLevel.addFreshEntity(IronNugget);
                }

            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
