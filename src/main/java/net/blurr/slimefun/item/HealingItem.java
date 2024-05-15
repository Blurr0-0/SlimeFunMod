package net.blurr.slimefun.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
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
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof Player player && player.getUsedItemHand() == InteractionHand.MAIN_HAND && !pLevel.isClientSide()) {
            healing(player);
            if (player.getMainHandItem().getItem() == ModItems.MEDICINE.get() && !player.isCreative()) {
                pLevel.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 0.6f, 1);
            }
            player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(player.getMainHandItem().getItem(), player.getMainHandItem().getCount() - 1));
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    public int getUseDuration(ItemStack pStack) {
        if (pStack.getItem() == ModItems.MEDICINE.get()) {
            return 32;
        } else {
            return 10;
        }
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        if (pStack.getItem() == ModItems.MEDICINE.get()) {
            return UseAnim.DRINK;
        } else {
            return UseAnim.BOW;
        }
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
