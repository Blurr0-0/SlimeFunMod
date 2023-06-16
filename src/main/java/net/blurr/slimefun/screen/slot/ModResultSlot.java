package net.blurr.slimefun.screen.slot;

import net.blurr.slimefun.recipe.EnhancedCraftingRecipe;
import net.blurr.slimefun.recipe.ModRecipes;
import net.blurr.slimefun.recipe.ShapedEnhancedCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ModResultSlot extends Slot {
    private final CraftingContainer craftSlots;
    private final Player player;
    private int removeCount;

    public ModResultSlot(Player pPlayer, CraftingContainer pCraftingCont, Container pCont, int i, int j, int f) {
        super(pCont, i, j, f);
        this.player = pPlayer;
        this.craftSlots = pCraftingCont;
    }

    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    public ItemStack remove(int i) {
        if (this.hasItem()) {
            this.removeCount += Math.min(i, this.getItem().getCount());
        }

        return super.remove(i);
    }

    protected void onQuickCraft(ItemStack pStack, int i) {
        this.removeCount += i;
        this.checkTakeAchievements(pStack);
    }

    protected void onSwapCraft(int i) {
        this.removeCount += i;
    }

    protected void checkTakeAchievements(ItemStack pStack) {
        if (this.removeCount > 0) {
            pStack.onCraftedBy(this.player.level, this.player, this.removeCount);
            net.minecraftforge.event.ForgeEventFactory.firePlayerCraftingEvent(this.player, pStack, this.craftSlots);
        }

        if (this.container instanceof RecipeHolder) {
            ((RecipeHolder)this.container).awardUsedRecipes(this.player);
        }

        this.removeCount = 0;
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
        NonNullList<ItemStack> nonnulllist = pPlayer.level.getRecipeManager().getRemainingItemsFor(EnhancedCraftingRecipe.Type.INSTANCE, this.craftSlots, pPlayer.level);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftSlots.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftSlots.removeItem(i, 1);
                itemstack = this.craftSlots.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftSlots.setItem(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftSlots.setItem(i, itemstack1);
                } else if (!this.player.getInventory().add(itemstack1)) {
                    this.player.drop(itemstack1, false);
                }
            }
        }

    }
}
