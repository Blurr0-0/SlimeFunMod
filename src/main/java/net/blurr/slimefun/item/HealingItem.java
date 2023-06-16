package net.blurr.slimefun.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealingItem extends Item {

    private int healAmount;

    private boolean extinguish;

    private boolean cure;

    public HealingItem(Properties pProperties, int pHealAmount, boolean pExtinguish, boolean pCure) {
        super(pProperties);
        this.healAmount = pHealAmount;
        this.extinguish = pExtinguish;
        this.cure = pCure;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND && !pLevel.isClientSide()) {
            healing(pPlayer);
            if (pPlayer.getMainHandItem().getItem() == ModItems.MEDICINE.get() && !pPlayer.isCreative()) {
                pLevel.playSound(null, pPlayer.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 0.6f, 1);
            }
            pPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(pPlayer.getMainHandItem().getItem(), pPlayer.getMainHandItem().getCount() - 1));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public void healing(Player pPlayer) {

        pPlayer.heal(healAmount);

        if (extinguish) {
            pPlayer.clearFire();
        }

        if (cure) {
            pPlayer.removeEffect(MobEffects.POISON);
            pPlayer.removeEffect(MobEffects.WITHER);
//            pPlayer.removeEffect(ModMobEffects.RADIATION);  TODO add radiation effect
        }

    }


}
