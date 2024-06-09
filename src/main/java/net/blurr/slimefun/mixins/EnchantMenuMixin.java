package net.blurr.slimefun.mixins;

import net.blurr.slimefun.event.ItemEvents;
import net.blurr.slimefun.event.ModEvents;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantMenuMixin {

    private EnchantmentMenu self() {
        return (EnchantmentMenu) (Object) this;
    }
    
    @Inject(method = "clickMenuButton", at = @At("RETURN"), remap = false)
    private void clickMenuButtonMixin(Player pPlayer, int pId, CallbackInfoReturnable<Boolean> info) {
        ItemStack pStack = self().getSlot(0).getItem();
        Event pEvent = new ModEvents.PlayerEnchantEvent(pPlayer, pStack, self());
        MinecraftForge.EVENT_BUS.post(pEvent);
    }
}