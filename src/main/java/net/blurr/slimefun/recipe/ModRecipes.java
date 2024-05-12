package net.blurr.slimefun.recipe;

import net.blurr.slimefun.SlimeFun;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SlimeFun.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ShapedEnhancedCraftingRecipe>> SHAPED_ENHANCED_CRAFTING_SERIALIZER =
            SERIALIZERS.register("enhanced_crafting_shaped", () -> ShapedEnhancedCraftingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<ShapelessEnhancedCraftingRecipe>> SHAPELESS_ENHANCED_CRAFTING_SERIALIZER =
            SERIALIZERS.register("enhanced_crafting_shapeless", () -> ShapelessEnhancedCraftingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<OrganicFoodRecipe>> ORGANIC_FOOD_SERIALIZER =
            SERIALIZERS.register("organic_food", () -> new SimpleRecipeSerializer<>(OrganicFoodRecipe::new));

    public static final RegistryObject<RecipeSerializer<AlloyingRecipe>> ALLOYING_SERIALIZER =
            SERIALIZERS.register("alloying", () -> AlloyingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

}
