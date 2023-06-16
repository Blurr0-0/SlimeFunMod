package net.blurr.slimefun.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public interface IOrganicFood {

    List<Item> FoodItems = new ArrayList<>() {{
        add(Items.WHEAT);
        add(Items.CARROT);
        add(Items.POTATO);
        add(Items.WHEAT_SEEDS);
        add(Items.BEETROOT);
        add(Items.MELON);
        add(Items.APPLE);
        add(Items.DRIED_KELP);
        add(Items.COCOA_BEANS);
        add(Items.SEAGRASS);
    }};

    String TAG_COLOR = "contents";
    String TAG_DISPLAY = "display";
    String DEFAULT_CONTENT = "None";

    static String getContents(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement("display");
        return compoundtag != null && compoundtag.contains("contents", 99) ? compoundtag.getString("contents") : "None";
    }

    static void setContents(ItemStack pStack, String contents) {
        pStack.getOrCreateTagElement("display").putString("contents", contents);
    }
    static ItemStack Content(ItemStack pStack, ItemStack pFood) {
        setContents(pStack, pFood.getItem().getName(pFood).getString());
        return pStack;
    }
}
