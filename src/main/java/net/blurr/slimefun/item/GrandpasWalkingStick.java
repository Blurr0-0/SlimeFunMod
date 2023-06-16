package net.blurr.slimefun.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GrandpasWalkingStick extends Item {

    public GrandpasWalkingStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.knockback(1f, pAttacker.getX() - pTarget.getX(), pAttacker.getZ() - pTarget.getZ());
        return true;
    }

}
