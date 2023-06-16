package net.blurr.slimefun.recipe;

import net.blurr.slimefun.item.IOrganicFood;
import net.blurr.slimefun.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class OrganicFoodRecipe extends ModCustomRecipe {
    public OrganicFoodRecipe(ResourceLocation pId) {
        super(pId);
    }

    public boolean matches(CraftingContainer pInv, Level pLevel) {
        ItemStack tinCan = ItemStack.EMPTY;
        ItemStack foodItem = ItemStack.EMPTY;

        for(int i = 0; i < pInv.getContainerSize(); ++i) {
            ItemStack itemstack1 = pInv.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == ModItems.TIN_CAN.get()) {
                    if (!tinCan.isEmpty()) {
                        return false;
                    }

                    tinCan = itemstack1;
                } else if (IOrganicFood.FoodItems.contains(itemstack1.getItem())) {
                    if (!foodItem.isEmpty()) {
                        return false;
                    }

                    foodItem = itemstack1;
                } else {
                    return false;
                }
            }
        }

        return !tinCan.isEmpty() && !foodItem.isEmpty() && tinCan.getCount() * 2 <= 64 && foodItem.getCount() * 2 <= 64;
    }

    public ItemStack assemble(CraftingContainer pInv) {
        ItemStack tinCan = ItemStack.EMPTY;
        ItemStack foodItem = ItemStack.EMPTY;

        for(int i = 0; i < pInv.getContainerSize(); ++i) {
            ItemStack itemstack1 = pInv.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == ModItems.TIN_CAN.get()) {
                    tinCan = itemstack1;
                } else if (IOrganicFood.FoodItems.contains(itemstack1.getItem())) {
                    foodItem = itemstack1;
                }
            }
        }

        ItemStack organicFood = new ItemStack(ModItems.ORGANIC_FOOD.get(), 2);
        IOrganicFood.Content(organicFood, foodItem);
        return organicFood;
    }

//    public boolean matches(CraftingContainer pInv, Level pLevel) {
//        if (pInv.getWidth() == 3 && pInv.getHeight() == 3) {
//            for(int i = 0; i < pInv.getWidth(); ++i) {
//                for(int j = 0; j < pInv.getHeight(); ++j) {
//                    ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());
//                    if (itemstack.isEmpty()) {
//                        return false;
//                    }
//
//                    if (i == 1 && j == 1) {
//                        if (!itemstack.is(ModItems.TIN_CAN.get())) {
//                            return false;
//                        }
//                    } else if (!IOrganicFood.FoodItems.contains(itemstack.getItem())) {
//                        return false;
//                    }
//                }
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Returns an Item that is the result of this recipe
//     */
//    public ItemStack assemble(CraftingContainer pInv) {
//        ItemStack itemstack = pInv.getItem(1 + pInv.getWidth());
//        if (!itemstack.is(ModItems.TIN_CAN.get())) {
//            return ItemStack.EMPTY;
//        } else {
//            ItemStack itemstack1 = new ItemStack(ModItems.ORGANIC_FOOD.get(), 2);
//            IOrganicFood.Content(itemstack1, itemstack);
//            return itemstack1;
//        }
//    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ORGANIC_FOOD_SERIALIZER.get();
    }

    public static class Type implements RecipeType<OrganicFoodRecipe> {
        public static final String ID = "organic_food";

    }
}
