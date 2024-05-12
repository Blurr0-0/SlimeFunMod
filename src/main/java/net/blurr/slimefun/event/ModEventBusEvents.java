package net.blurr.slimefun.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

import static net.minecraft.world.item.crafting.RecipeType.register;

@Mod.EventBusSubscriber(modid = SlimeFun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(

        );
    }

    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
//        Registry.register(Registry.RECIPE_TYPE, EnhancedCraftingRecipe.Type.ID, EnhancedCraftingRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, ShapedEnhancedCraftingRecipe.Type.ID, EnhancedCraftingRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, ShapelessEnhancedCraftingRecipe.Type.ID, EnhancedCraftingRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, OrganicFoodRecipe.Type.ID, EnhancedCraftingRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, AlloyingRecipe.Type.ID, AlloyingRecipe.Type.INSTANCE);
    }

}
