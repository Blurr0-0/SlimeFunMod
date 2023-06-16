package net.blurr.slimefun.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrganicFood extends Item implements IOrganicFood {
    public OrganicFood(Properties pProperties) {
        super(pProperties);
    }

//    @Override
//    public Component getName(ItemStack stack) {
//        String itemName = this.getDescriptionId(stack);
//        String content = IOrganicFood.getContents(stack);
//        Component hoverText = new TextComponent("Content: ").append(new TextComponent(content));
//        return new TextComponent(itemName).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
//    }

    //    @Override
//    public String getDescriptionId(ItemStack pStack) {
//        return this.getDescriptionId() + "Content:" + IOrganicFood.getContents(pStack);
//    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("Content:" + IOrganicFood.getContents(pStack)));

    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        CompoundTag compoundtag = pStack.getTagElement("display");
        if (compoundtag != null && compoundtag.contains("contents", 99)) {

        }

    }
}
