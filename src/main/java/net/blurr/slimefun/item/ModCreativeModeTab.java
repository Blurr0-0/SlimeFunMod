package net.blurr.slimefun.item;

import net.blurr.slimefun.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {

    public static final CreativeModeTab WEAPONS = new CreativeModeTab("weapons") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BLADE_OF_VAMPIRES.get());
        }
    };

    public static final CreativeModeTab FOOD = new CreativeModeTab("food") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FORTUNE_COOKIE.get());
        }
    };

    public static final CreativeModeTab USEFUL_ITEMS = new CreativeModeTab("useful_items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PORTABLE_CRAFTER.get());
        }
    };

    public static final CreativeModeTab BASIC_MACHINES = new CreativeModeTab("basic_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.ENHANCED_CRAFTING_TABLE.get());
        }
    };

    public static final CreativeModeTab TOOLS = new CreativeModeTab("tools") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GOLD_PAN.get());
        }
    };

    public static final CreativeModeTab RESOURCES = new CreativeModeTab("resources") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SYNTHETIC_SAPPHIRE.get());
        }
    };

    public static final CreativeModeTab MISCELLANEOUS = new CreativeModeTab("miscellaneous") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SIFTED_ORE.get());
        }
    };

    public static final CreativeModeTab T1_TALISMANS = new CreativeModeTab("t1_talismans") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BRASS_INGOT.get());
        }
    };

}
