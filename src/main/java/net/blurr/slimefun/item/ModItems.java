package net.blurr.slimefun.item;

import net.blurr.slimefun.SlimeFun;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {

    public static final FoodProperties DIET_COOKIE_FOOD = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0), 1).build();
    public static final FoodProperties MEAT_JERKY = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).build();
    public static final FoodProperties MAGIC_SUGAR_FOOD = (new FoodProperties.Builder()).nutrition(1).saturationMod(0).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 4), 1).build();
    public static final FoodProperties MONSTER_JERKY_FOOD = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.8F).effect(new MobEffectInstance(MobEffects.SATURATION, 5, 0), 1).build();

    //TODO add tab for sifted ore and for stone chunk, add cooler when juice is finished, finish backpack(make it say the items on the inside)

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SlimeFun.MOD_ID);

    public static final RegistryObject<Item> SIFTED_ORE = ITEMS.register("sifted_ore",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> WHEAT_FLOUR = ITEMS.register("wheat_flour",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> HOOK = ITEMS.register("hook",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> STONE_CHUNK = ITEMS.register("stone_chunk",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> SALT = ITEMS.register("salt",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> HEAVY_CREAM = ITEMS.register("heavy_cream",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> BUTTER = ITEMS.register("butter",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> CRUSHED_ORE = ITEMS.register("crushed_ore",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> PULVERIZED_ORE = ITEMS.register("pulverized_ore",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> PURE_ORE_CLUSTER = ITEMS.register("pure_ore_cluster",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> TINY_PILE_OF_URANIUM = ITEMS.register("tiny_pile_of_uranium",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> SMALL_CHUNK_OF_URANIUM = ITEMS.register("small_chunk_of_uranium",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> CLOTH = ITEMS.register("cloth",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> TIN_CAN = ITEMS.register("tin_can",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> CARAT_GOLD_BLOCK = ITEMS.register("24_carat_gold_block",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> REINFORCED_PLATE = ITEMS.register("reinforced_plate",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> DUCT_TAPE = ITEMS.register("duct_tape",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> ORGANIC_FOOD = ITEMS.register("organic_food",
            () -> new OrganicFood(new Item.Properties()
                    .tab(ModCreativeModeTab.MISCELLANEOUS)));

    public static final RegistryObject<Item> REINFORCED_ALLOY_INGOT = ITEMS.register("reinforced_alloy_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> HARDENED_METAL = ITEMS.register("hardened_metal",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> DAMASCUS_STEEL_INGOT = ITEMS.register("damascus_steel_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> DURALUMIN_INGOT = ITEMS.register("duralumin_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> BILLON_INGOT = ITEMS.register("billon_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ALUMINUM_BRASS_INGOT = ITEMS.register("aluminum_brass_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ALUMINUM_BRONZE_INGOT = ITEMS.register("aluminum_bronze_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> CORINTHIAN_BRONZE_INGOT = ITEMS.register("corinthian_bronze_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SOLDER_INGOT = ITEMS.register("solder_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SYNTHETIC_SAPPHIRE = ITEMS.register("synthetic_sapphire",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SYNTHETIC_DIAMOND = ITEMS.register("synthetic_diamond",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> RAW_CARBONADO = ITEMS.register("raw_carbonado",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> CARBONADO = ITEMS.register("carbonado",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> FERROSILICON = ITEMS.register("ferrosilicon",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> TIN_DUST = ITEMS.register("tin_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SILVER_DUST = ITEMS.register("silver_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> LEAD_DUST = ITEMS.register("lead_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ALUMINUM_DUST = ITEMS.register("aluminum_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ZINC_DUST = ITEMS.register("zinc_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> MAGNESIUM_DUST = ITEMS.register("magnesium_dust",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> MAGNESIUM_INGOT = ITEMS.register("magnesium_ingot",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> SULFATE = ITEMS.register("sulfate",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.RESOURCES)));

    public static final RegistryObject<Item> GOLD_PAN = ITEMS.register("gold_pan",
            () -> new GoldPan(new Item.Properties()
                    .stacksTo(1)
                    .durability(512)
                    .tab(ModCreativeModeTab.TOOLS)));

    public static final RegistryObject<Item> NETHER_GOLD_PAN = ITEMS.register("nether_gold_pan",
            () -> new NetherGoldPan(new Item.Properties()
                    .stacksTo(1)
                    .durability(512)
                    .tab(ModCreativeModeTab.TOOLS)));


    public static final RegistryObject<Item> SMELTERS_PICKAXE = ITEMS.register("smelters_pickaxe",
            () -> new PickaxeItem(Tiers.IRON, 1, -2.8F, (new Item.Properties())
                    .tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> LUMBER_AXE = ITEMS.register("lumber_axe",
            () -> new AxeItem(Tiers.IRON, 6.0F, -3.1F, (new Item.Properties())
                    .tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> GRANDMAS_WALKING_STICK = ITEMS.register("grandmas_walking_stick",
            () -> new GrandmasWalkingStick(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.WEAPONS)));

    public static final RegistryObject<Item> GRANDPAS_WALKING_STICK = ITEMS.register("grandpas_walking_stick",
            () -> new GrandpasWalkingStick(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.WEAPONS)));

    public static final RegistryObject<Item> SWORD_OF_BEHEADING = ITEMS.register("sword_of_beheading",
            () -> new SwordOfBeheading(Tiers.DIAMOND, 3, -2.4F, new Item.Properties()
                    .stacksTo(1)
                    .durability(1032)
                    .tab(ModCreativeModeTab.WEAPONS)));

    public static final RegistryObject<Item> BLADE_OF_VAMPIRES = ITEMS.register("blade_of_vampires",
            () -> new BladeOfVampires(Tiers.DIAMOND, 4, -2.4F, new Item.Properties()
                    .stacksTo(1)
                    .durability(1032)
                    .tab(ModCreativeModeTab.WEAPONS)));

    public static final RegistryObject<Item> PORTABLE_CRAFTER = ITEMS.register("portable_crafter",
            () -> new PortableCrafter(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS)));

    public static final RegistryObject<Item> PORTABLE_DUSTBIN = ITEMS.register("portable_dustbin",
            () -> new PortableDustbin(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS)));

    public static final RegistryObject<Item> RAG = ITEMS.register("rag",
            () -> new HealingItem(new Item.Properties()
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 4, true, false));

    public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage",
            () -> new HealingItem(new Item.Properties()
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 8, true, false));

    public static final RegistryObject<Item> SPLINT = ITEMS.register("splint",
            () -> new HealingItem(new Item.Properties()
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 4, false, false));

    public static final RegistryObject<Item> VITAMINS = ITEMS.register("vitamins",
            () -> new HealingItem(new Item.Properties()
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 8, true, true));

    public static final RegistryObject<Item> MEDICINE = ITEMS.register("medicine",
            () -> new HealingItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 8, true, true));

    public static final RegistryObject<Item> SMALL_BACKPACK = ITEMS.register("small_backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 1));

    public static final RegistryObject<Item> BACKPACK = ITEMS.register("backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 2));

    public static final RegistryObject<Item> LARGE_BACKPACK = ITEMS.register("large_backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 3));

    public static final RegistryObject<Item> WOVEN_BACKPACK = ITEMS.register("woven_backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 4));

    public static final RegistryObject<Item> GILDED_BACKPACK = ITEMS.register("gilded_backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 5));

    public static final RegistryObject<Item> RADIANT_BACKPACK = ITEMS.register("radiant_backpack",
            () -> new BackpackItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS), 6));

    //cooler

    public static final RegistryObject<Item> TAPE_MEASURE = ITEMS.register("tape_measure",
            () -> new TapeMeasure(new Item.Properties()
                    .stacksTo(1)
                    .tab(ModCreativeModeTab.USEFUL_ITEMS)));

    public static final RegistryObject<Item> FORTUNE_COOKIE = ITEMS.register("fortune_cookie",
            () -> new FortuneCookie(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(Foods.COOKIE)));

    public static final RegistryObject<Item> DIET_COOKIE = ITEMS.register("diet_cookie",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(DIET_COOKIE_FOOD)));

    public static final RegistryObject<Item> BEEF_JERKY = ITEMS.register("beef_jerky",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MEAT_JERKY)));

    public static final RegistryObject<Item> PORK_JERKY = ITEMS.register("pork_jerky",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MEAT_JERKY)));

    public static final RegistryObject<Item> CHICKEN_JERKY = ITEMS.register("chicken_jerky",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MEAT_JERKY)));

    public static final RegistryObject<Item> MUTTON_JERKY = ITEMS.register("mutton_jerky",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MEAT_JERKY)));

    public static final RegistryObject<Item> FISH_JERKY = ITEMS.register("fish_jerky",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MEAT_JERKY)));

    public static final RegistryObject<Item> KELP_COOKIE = ITEMS.register("kelp_cookie",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(Foods.COOKIE)));

    public static final RegistryObject<Item> MAGIC_SUGAR = ITEMS.register("magic_sugar",
            () -> new Item(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MAGIC_SUGAR_FOOD)));

    public static final RegistryObject<Item> MONSTER_JERKY = ITEMS.register("monster_jerky",
            () -> new MonsterJekry(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD).food(MONSTER_JERKY_FOOD)));

    public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F, null));

    public static final RegistryObject<Item> CARROT_JUICE = ITEMS.register("carrot_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F, null));

    public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F, null));

    public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F, null));

    public static final RegistryObject<Item> SWEET_BERRY_JUICE = ITEMS.register("sweet_berry_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F, null));

    public static final RegistryObject<Item> GOLDEN_APPLE_JUICE = ITEMS.register("golden_apple_juice",
            () -> new JuiceItem(new Item.Properties()
                    .tab(ModCreativeModeTab.FOOD), 3, 0.4F,  new MobEffectInstance(MobEffects.ABSORPTION, 400, 0)));







    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
