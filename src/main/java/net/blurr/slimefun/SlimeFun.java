package net.blurr.slimefun;

import com.mojang.logging.LogUtils;
import net.blurr.slimefun.block.ModBlocks;
import net.blurr.slimefun.block.entity.ModBlockEntities;
import net.blurr.slimefun.item.ModItems;
import net.blurr.slimefun.recipe.ModRecipes;
import net.blurr.slimefun.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.obj.OBJLoader;
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
//        eventBus.addListener(this::onRenderWorldLast);

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

//    @SubscribeEvent
//    public void onRenderWorldLast(RenderLevelStageEvent event) {
//        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS){
//            Minecraft mc = Minecraft.getInstance();
//            Entity entity = mc.cameraEntity;
//            if (entity == null) return;
//            if (entity instanceof Player player) {
//                PoseStack matrixStack = event.getPoseStack();
//                VoxelShape FULL_BLOCK = Shapes.box(0, 0, 0, 1, 1, 1);
//                RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                RenderSystem.enableBlend();
//                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//                RenderSystem.disableCull();
//                RenderSystem.depthMask(false);
//                LumberAxeEvent.drawOutlineBoxes(Tesselator.getInstance(), matrixStack, Tesselator.getInstance().getBuilder(), getCameraOffset(new Vector3d(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z), player.getOnPos()), new Color(255, 0, 0, 255), FULL_BLOCK);
//            }
//        }
//    }

//    public Vector3d getCameraOffset(Vector3d camera, BlockPos pos) {
//        double xDif = (double) pos.getX() - camera.x;
//        double yDif = (double) pos.getY() - camera.y;
//        double zDif = (double) pos.getZ() - camera.z;
//        return new Vector3d(xDif, yDif, zDif);
//    }
}
