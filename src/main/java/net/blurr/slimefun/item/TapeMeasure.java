package net.blurr.slimefun.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import java.text.DecimalFormat;

public class TapeMeasure extends Item {
    public TapeMeasure(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player pPlayer = pContext.getPlayer();
        Level pLevel = pContext.getLevel();
        ItemStack item = pPlayer.getMainHandItem();
        CompoundTag anchorTag = item.getOrCreateTag();
        if (!pLevel.isClientSide) {
            if (pPlayer.isCrouching()) {
                BlockPos pAnchor = pContext.getClickedPos();
                anchorTag.putInt("x", pAnchor.getX());
                anchorTag.putInt("y", pAnchor.getY());
                anchorTag.putInt("z", pAnchor.getZ());
                String anchorPos = "§e" + (pAnchor.getX() + " | " + pAnchor.getY() + " | " + pAnchor.getZ());
                pPlayer.sendMessage(new TranslatableComponent("tape_measure.set_anchor").append(anchorPos), pPlayer.getUUID());

            }
            if (!pPlayer.isCrouching()) {
                if (item.hasTag()) {
                    BlockPos pEnd = pContext.getClickedPos();
                    double x1 = anchorTag.getInt("x");
                    double x2 = pEnd.getX();
                    double y1 = anchorTag.getInt("y");
                    double y2 = pEnd.getY();
                    double z1 = anchorTag.getInt("z");
                    double z2 = pEnd.getZ();
                    double pDistance = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2));
                    pDistance = Double.parseDouble(new DecimalFormat("##.###").format(pDistance));
                    pPlayer.sendMessage(new TranslatableComponent("tape_measure.measurement_taken").append("§e" + pDistance), pPlayer.getUUID());
                } else {
                    pPlayer.sendMessage(new TranslatableComponent("tape_measure.error"), pPlayer.getUUID());
                }
            }
        }
        return super.useOn(pContext);
    }

}
