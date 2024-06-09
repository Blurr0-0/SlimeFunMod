package net.blurr.slimefun.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ModEvents{

    //the PlayerEnchantEvent is called by the EnchantMenuMixin
    public static class PlayerEnchantEvent extends Event {
        private final Player player;
        private final ItemStack itemStack;
        private final EnchantmentMenu enchantmentMenu;
        private final int seed;

        public PlayerEnchantEvent(Player pPlayer, ItemStack pEnchanted, EnchantmentMenu pMenu) {
            this.player = pPlayer;
            this.itemStack = pEnchanted;
            this.enchantmentMenu = pMenu;
            this.seed = pMenu.getEnchantmentSeed();
        }

        public Player getPlayer() {
            return player;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public EnchantmentMenu getEnchantmentMenu() {
            return enchantmentMenu;
        }

        public int getSeed() {
            return seed;
        }
    }
}
