package net.blurr.slimefun.item;

import net.blurr.slimefun.screen.PortableDustbinMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class PortableDustbin extends Item {

    private static final Component CONTAINER_TITLE = new TranslatableComponent("container.delete_items");
    public PortableDustbin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player pPlayer = pContext.getPlayer();
        Level pLevel = pPlayer.getLevel();
        BlockPos pPos = new BlockPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
        BlockState pState = pLevel.getBlockState(pPos);
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.openMenu(getMenuProvider(pLevel, pPos));


            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        BlockPos pPos = new BlockPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());

        if (pLevel.isClientSide) {
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        } else {
            NetworkHooks.openGui((ServerPlayer) pPlayer, getMenuProvider(pLevel, pPos));
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }

    }

    public MenuProvider getMenuProvider(Level pLevel, BlockPos pPos) {

        return new SimpleMenuProvider((id, pInv, pPlayer) ->
                PortableDustbinMenu.threeRows(id, pInv), CONTAINER_TITLE);

    }


}
