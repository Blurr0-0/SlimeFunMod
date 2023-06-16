package net.blurr.slimefun;

import com.mojang.logging.LogUtils;
import net.blurr.slimefun.block.ModBlocks;
import net.blurr.slimefun.block.entity.ModBlockEntities;
import net.blurr.slimefun.item.ModItems;
import net.blurr.slimefun.recipe.ModRecipes;
import net.blurr.slimefun.screen.EnhancedCraftingTableScreen;
import net.blurr.slimefun.screen.ModMenuTypes;
import net.blurr.slimefun.screen.OreWasherScreen;
import net.blurr.slimefun.screen.SmelteryScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(SlimeFun.MOD_ID)
public class SlimeFun
{

    public static final String MOD_ID = "slimefun";

    private static final Logger LOGGER = LogUtils.getLogger();

    public SlimeFun()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModRecipes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {

        MenuScreens.register(ModMenuTypes.ENHANCED_CRAFTING_TABLE_MENU.get(), EnhancedCraftingTableScreen::new);
        MenuScreens.register(ModMenuTypes.ORE_WASHER_MENU.get(), OreWasherScreen::new);
        MenuScreens.register(ModMenuTypes.SMELTERY_MENU.get(), SmelteryScreen::new);

    }

    private void setup(final FMLCommonSetupEvent event)
    {

        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
