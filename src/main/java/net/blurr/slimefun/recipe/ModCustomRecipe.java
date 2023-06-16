package net.blurr.slimefun.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class ModCustomRecipe implements EnhancedCraftingRecipe {
    private final ResourceLocation id;

    public ModCustomRecipe(ResourceLocation pId) {
        this.id = pId;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean isSpecial() {
        return true;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}