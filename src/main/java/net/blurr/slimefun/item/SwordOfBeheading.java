package net.blurr.slimefun.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class SwordOfBeheading extends SwordItem {
    public SwordOfBeheading(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget instanceof Player) {
            if (pTarget.getHealth() <= 0 && pAttacker.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                if (new Random().nextInt(100) < 70) {
                    ItemStack head = new ItemStack(Items.PLAYER_HEAD, 1);
                    CompoundTag tag = new CompoundTag();
                    tag.putString("SkullOwner", pTarget.getName().getString());
                    head.setTag(tag);
                    ItemEntity droppedItem = new ItemEntity(pTarget.getLevel(), pTarget.getX(), pTarget.getY(), pTarget.getZ(), head);
                    pTarget.getLevel().addFreshEntity(droppedItem);
                }
            }
        } else if (pTarget instanceof Zombie) {
            if (pTarget.getHealth() <= 0 && pAttacker.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                if (new Random().nextInt(100) < 40) {
                    ItemStack head = new ItemStack(Items.ZOMBIE_HEAD, 1);
                    ItemEntity droppedItem = new ItemEntity(pTarget.getLevel(), pTarget.getX(), pTarget.getY(), pTarget.getZ(), head);
                    pTarget.getLevel().addFreshEntity(droppedItem);
                }
            }

        } else if (pTarget instanceof Skeleton) {
            if (pTarget.getHealth() <= 0 && pAttacker.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                if (new Random().nextInt(100) < 40) {
                    ItemStack head = new ItemStack(Items.SKELETON_SKULL, 1);
                    ItemEntity droppedItem = new ItemEntity(pTarget.getLevel(), pTarget.getX(), pTarget.getY(), pTarget.getZ(), head);
                    pTarget.getLevel().addFreshEntity(droppedItem);
                }
            }

        } else if (pTarget instanceof Creeper) {
            if (pTarget.getHealth() <= 0 && pAttacker.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                if (new Random().nextInt(100) < 40) {
                    ItemStack head = new ItemStack(Items.CREEPER_HEAD, 1);
                    ItemEntity droppedItem = new ItemEntity(pTarget.getLevel(), pTarget.getX(), pTarget.getY(), pTarget.getZ(), head);
                    pTarget.getLevel().addFreshEntity(droppedItem);
                }
            }

        } else if (pTarget instanceof WitherSkeleton) {
            if (pTarget.getHealth() <= 0 && pAttacker.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this) {
                if (new Random().nextInt(100) < 25) {
                    ItemStack head = new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
                    ItemEntity droppedItem = new ItemEntity(pTarget.getLevel(), pTarget.getX(), pTarget.getY(), pTarget.getZ(), head);
                    pTarget.getLevel().addFreshEntity(droppedItem);
                }
            }

        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
