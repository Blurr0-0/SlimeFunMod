package net.blurr.slimefun.block.entity;

import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SlimeFun.MOD_ID);



    public static final RegistryObject<BlockEntityType<OreWasherEntity>> ORE_WASHER_ENTITY =
            BLOCK_ENTITIES.register("ore_washer_entity", () ->
                    BlockEntityType.Builder.of(OreWasherEntity::new,
                            ModBlocks.ORE_WASHER.get()).build(null));

    public static final RegistryObject<BlockEntityType<SmelteryEntity>> SMELTERY_ENTITY =
            BLOCK_ENTITIES.register("smeltery_entity", () ->
                    BlockEntityType.Builder.of(SmelteryEntity::new,
                            ModBlocks.SMELTERY.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
