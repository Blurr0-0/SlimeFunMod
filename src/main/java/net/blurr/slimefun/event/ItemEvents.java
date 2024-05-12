package net.blurr.slimefun.event;

import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;
import java.util.Stack;

@Mod.EventBusSubscriber(modid = SlimeFun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEvents {
    @SubscribeEvent
    public static void LumberAxeBreak(BlockEvent.BreakEvent event) {
        BlockPos pStartPos = event.getPos();
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        BlockState pState = event.getState();
//        renderBox(matrixStack, pStartPos.getX(), pStartPos.getY(), pStartPos.getZ(), pStartPos.getX() + 1, pStartPos.getY() + 1, pStartPos.getZ() + 1);

        Stack<BlockPos> positionsToBreak = new Stack<>();
        positionsToBreak.push(pStartPos);

        int logsBroken = 0;

        while (!positionsToBreak.isEmpty() && logsBroken < 100 && pPlayer.isHolding(ModItems.LUMBER_AXE.get()) && !pPlayer.isCrouching()) {
            BlockPos currentPosition = positionsToBreak.pop();
            if (checkForLogs(currentPosition, pLevel)) {
                //when broken
                pLevel.playSound(pPlayer, currentPosition, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1f, 1f);
                if (!pPlayer.isCreative()) {
                    pLevel.destroyBlock(currentPosition, true, pPlayer);
                    pPlayer.getMainHandItem().hurt(1, new Random(), (ServerPlayer) pPlayer);

                } else {
                    pLevel.destroyBlock(currentPosition, false, pPlayer);
                }
                logsBroken++;

                // Add adjacent positions to the stack for further processing
                positionsToBreak.push(currentPosition.above());
                positionsToBreak.push(currentPosition.below());
                positionsToBreak.push(currentPosition.north());
                positionsToBreak.push(currentPosition.south());
                positionsToBreak.push(currentPosition.west());
                positionsToBreak.push(currentPosition.east());
            }
        }
    }
    private static Boolean checkForLogs(BlockPos pPos, Level pLevel) {
        TagKey[] tags = pLevel.getBlockState(pPos).getTags().toArray(net.minecraft.tags.TagKey[]::new);

        for (int i=0; i < tags.length; ++i) {
            if (tags[i] == BlockTags.LOGS) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void SmelterPickaxeBreak(BlockEvent.BreakEvent event) {
        BlockPos pPos = event.getPos();
        Player pPlayer = event.getPlayer();
        Level pLevel = pPlayer.getLevel();
        BlockState pState = event.getState();

        if (!pPlayer.isCreative() && pPlayer.isHolding(ModItems.SMELTERS_PICKAXE.get())) {
            List<ItemStack> drops = Block.getDrops(pState, (ServerLevel) pLevel, pPos, pLevel.getBlockEntity(pPos));

            for (int i = 0; i < drops.size(); i++) {
                ItemStack pStack = drops.get(i);
                ItemStack smeltedStack = smelt(pStack, pLevel);
                ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, smeltedStack);
                pLevel.addFreshEntity(itemEntity);
                drops.set(i, smeltedStack); // Replace the original stack with the smelted stack
            }


            pLevel.destroyBlock(pPos, false, pPlayer);

        }

    }

    private static ItemStack smelt(ItemStack stack, Level level) {
        SmeltingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), level).orElse(null);
        if (recipe != null) {
            ItemStack result = recipe.getResultItem().copy();
            result.setCount(stack.getCount() * result.getCount());
            return result;
        }
        return stack;
    }

//    public static void renderBox(PoseStack matrixStack, int sX, int sY, int sZ, int eX, int eY, int eZ) {
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//
//        float red = 1.0F;
//        float green = 0.0F;
//        float blue = 0.0F;
//        float alpha = 0.5F;
//
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.disableTexture();
//        RenderSystem.enableDepthTest();
//
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferBuilder = tesselator.getBuilder();
//
//        matrixStack.pushPose();
//
//        bufferBuilder.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
//
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, sY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, eY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, eY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, sY, sZ).color(red, green, blue, alpha).endVertex();
//
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, sY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, sY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, sY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, sY, sZ).color(red, green, blue, alpha).endVertex();
//
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, sY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, eY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, eY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, sY, eZ).color(red, green, blue, alpha).endVertex();
//
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, eY, eZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), sX, eY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, eY, sZ).color(red, green, blue, alpha).endVertex();
//        bufferBuilder.vertex(matrixStack.last().pose(), eX, eY, eZ).color(red, green, blue, alpha).endVertex();
//
//        tesselator.end();
//
//        matrixStack.popPose();
//
//        RenderSystem.enableTexture();
//        RenderSystem.disableBlend();
//    }

//    public static void drawBox(PoseStack.Pose entry, BufferBuilder buffer, Vector3d cameraOffset, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color color) {
//        minX = minX + (float) cameraOffset.x;
//        minY = minY + (float) cameraOffset.y;
//        minZ = minZ + (float) cameraOffset.z;
//        maxX = maxX + (float) cameraOffset.x;
//        maxY = maxY + (float) cameraOffset.y;
//        maxZ = maxZ + (float) cameraOffset.z;
//
//        Matrix4f position = entry.pose();
//        float r = color.getRed() / 255f;
//        float g = color.getGreen() / 255f;
//        float b = color.getBlue() / 255f;
//        float a = color.getAlpha() / 255f;
//        // West
//        buffer.vertex(position, minX, minY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, maxY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, maxY, minZ).color(r, g, b, a).endVertex();
//
//        // East
//        buffer.vertex(position, maxX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, minY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
//
//        // North
//        buffer.vertex(position, maxX, minY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, minY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, maxY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, minZ).color(r, g, b, a).endVertex();
//
//        // South
//        buffer.vertex(position, minX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, maxY, maxZ).color(r, g, b, a).endVertex();
//
//        // Top
//        buffer.vertex(position, minX, maxY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, maxY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, maxY, minZ).color(r, g, b, a).endVertex();
//
//        // Bottom
//        buffer.vertex(position, maxX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, minY, maxZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, minX, minY, minZ).color(r, g, b, a).endVertex();
//        buffer.vertex(position, maxX, minY, minZ).color(r, g, b, a).endVertex();
//    }

//    public static void drawOutlineBoxes(Tesselator tessellator, PoseStack matrices, BufferBuilder buffer, Vector3d camDif, Color color, VoxelShape outline) {
//        PoseStack.Pose entry = matrices.last();
//
//        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        // Divide into each edge and draw all of them
//        outline.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
//            // Fix Z fighting
//            minX -= .001;
//            minY -= .001;
//            minZ -= .001;
//            maxX += .001;
//            maxY += .001;
//            maxZ += .001;
//            drawBox(entry, buffer, camDif, (float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ, color);
//        });
//        tessellator.end();
//    }

}
