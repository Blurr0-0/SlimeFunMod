package net.blurr.slimefun.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnchantmentUtil {

    private static final Map<Enchantment, Set<Enchantment>> EXCLUSIVE_ENCHANTMENTS = new HashMap<>();

    static {
        // Initialize exclusive enchantments based on the rules
        addConflict(Enchantments.SHARPNESS, Enchantments.SMITE);
        addConflict(Enchantments.SHARPNESS, Enchantments.BANE_OF_ARTHROPODS);
        addConflict(Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS);
        
        addConflict(Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.BLAST_PROTECTION);
        addConflict(Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.FIRE_PROTECTION);
        addConflict(Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.PROJECTILE_PROTECTION);
        addConflict(Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION);
        addConflict(Enchantments.BLAST_PROTECTION, Enchantments.PROJECTILE_PROTECTION);
        addConflict(Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION);
        
        addConflict(Enchantments.SILK_TOUCH, Enchantments.BLOCK_FORTUNE);
        addConflict(Enchantments.SILK_TOUCH, Enchantments.MOB_LOOTING);
        addConflict(Enchantments.SILK_TOUCH, Enchantments.FISHING_LUCK);
        
        addConflict(Enchantments.DEPTH_STRIDER, Enchantments.FROST_WALKER);
        
        addConflict(Enchantments.MENDING, Enchantments.INFINITY_ARROWS);
        
        addConflict(Enchantments.RIPTIDE, Enchantments.LOYALTY);
        addConflict(Enchantments.RIPTIDE, Enchantments.CHANNELING);
        
        addConflict(Enchantments.MULTISHOT, Enchantments.PIERCING);
    }

    private static void addConflict(Enchantment ench1, Enchantment ench2) {
        EXCLUSIVE_ENCHANTMENTS.computeIfAbsent(ench1, k -> new HashSet<>()).add(ench2);
        EXCLUSIVE_ENCHANTMENTS.computeIfAbsent(ench2, k -> new HashSet<>()).add(ench1);
    }

    /**
     * Checks if adding a new enchantment to an item would cause any conflicts with existing enchantments.
     *
     * @param itemStack      The item stack to check.
     * @param newEnchantment The new enchantment to add.
     * @return true if there are no conflicts, false otherwise.
     */
    public static boolean canAddEnchantment(ItemStack itemStack, Enchantment newEnchantment) {
        Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(itemStack);

        for (Enchantment existing : existingEnchantments.keySet()) {
            if (existing == newEnchantment || EXCLUSIVE_ENCHANTMENTS.getOrDefault(existing, new HashSet<>()).contains(newEnchantment)) {
                return false; // Conflict found
            }
        }
        return true; // No conflicts
    }
}