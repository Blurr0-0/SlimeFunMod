package net.blurr.slimefun.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OrganicFood extends Item implements IOrganicFood {
    public OrganicFood(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        CompoundTag compoundtag = pStack.getTagElement("display");
        pTooltip.add(new TranslatableComponent("organic_food.contents")
                .append(" ")
                .append(IOrganicFood.getContents(compoundtag))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        Player pPlayer = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        CompoundTag compoundtag = pStack.getTagElement("display");
        pPlayer.sendMessage(new TextComponent(IOrganicFood.getContents(compoundtag)), pPlayer.getUUID());

        return super.useOn(pContext);
    }
}
