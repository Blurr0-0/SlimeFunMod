package net.blurr.slimefun.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MonsterJekry extends Item {
    public MonsterJekry(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pLivingEntity.removeEffect(MobEffects.HUNGER);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
