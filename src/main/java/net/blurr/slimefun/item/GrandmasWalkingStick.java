package net.blurr.slimefun.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class GrandmasWalkingStick extends Item {

    public GrandmasWalkingStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.knockback(0.5f, pAttacker.getX() - pTarget.getX(), pAttacker.getZ() - pTarget.getZ());
        return true;
    }

    //    @Override
//    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
//        entity.knockback(2f, entity.getX(), entity.getZ());
//        return true;
//    }
}
