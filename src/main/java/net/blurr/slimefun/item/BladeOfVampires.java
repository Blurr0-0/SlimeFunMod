package net.blurr.slimefun.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.Random;

public class BladeOfVampires extends SwordItem {
    public BladeOfVampires(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.setSecondsOnFire(8);
        if (new Random().nextInt(100) < 45) {
            pAttacker.heal(4);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
