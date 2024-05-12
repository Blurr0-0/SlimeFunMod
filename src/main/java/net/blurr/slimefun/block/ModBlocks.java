package net.blurr.slimefun.block;

import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.item.ModCreativeModeTab;
import net.blurr.slimefun.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    //TODO add other 2 recipes to ore washer and crafting recipe for it also texture for it and the enhanced crafting table, make sure every text is translatable, finish block placer

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SlimeFun.MOD_ID);



    public static final RegistryObject<Block> ENHANCED_CRAFTING_TABLE = registerBlock("enhanced_crafting_table",
            () -> new EnhancedCraftingTable(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(3f)
                    .sound(SoundType.WOOD)),
            ModCreativeModeTab.BASIC_MACHINES);

    public static final RegistryObject<Block> ORE_WASHER = registerBlock("ore_washer",
            () -> new OreWasher(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(3f)
                    .sound(SoundType.WOOD)),
            ModCreativeModeTab.BASIC_MACHINES);

    public static final RegistryObject<Block> SMELTERY = registerBlock("smeltery",
            () -> new Smeltery(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(3f)
                    .sound(SoundType.WOOD)),
            ModCreativeModeTab.BASIC_MACHINES);

    public static final RegistryObject<Block> BLOCK_PLACER = registerBlock("block_placer",
            () -> new BlockPlacer(BlockBehaviour.Properties
                    .of(Material.WOOD)
                    .strength(3f)
                    .sound(SoundType.WOOD)),
            ModCreativeModeTab.BASIC_MACHINES);



    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
