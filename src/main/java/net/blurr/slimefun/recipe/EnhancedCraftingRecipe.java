package net.blurr.slimefun.recipe;

import net.blurr.slimefun.SlimeFun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface EnhancedCraftingRecipe extends Recipe<CraftingContainer> {

//    ResourceLocation ID = new ResourceLocation(SlimeFun.MOD_ID, "enhanced_crafting");

    default RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    class Type implements RecipeType<EnhancedCraftingRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "enhanced_crafting";
    }
}
