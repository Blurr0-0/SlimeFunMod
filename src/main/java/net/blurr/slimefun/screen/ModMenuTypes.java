package net.blurr.slimefun.screen;

import net.blurr.slimefun.SlimeFun;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, SlimeFun.MOD_ID);



    public static final RegistryObject<MenuType<EnhancedCraftingTableMenu>> ENHANCED_CRAFTING_TABLE_MENU =
            registerMenuType(EnhancedCraftingTableMenu::new, "enhanced_crafting_table_menu");

    public static final RegistryObject<MenuType<OreWasherMenu>> ORE_WASHER_MENU =
            registerMenuType(OreWasherMenu::new, "ore_washer_menu");

    public static final RegistryObject<MenuType<SmelteryMenu>> SMELTERY_MENU =
            registerMenuType(SmelteryMenu::new, "smeltery_menu");



    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
