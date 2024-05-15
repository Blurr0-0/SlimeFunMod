package net.blurr.slimefun.item;

import io.netty.buffer.Unpooled;
import net.blurr.slimefun.item.capability.BackPackCapability;
import net.blurr.slimefun.screen.BackpackMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends Item {

    public int rowAmount;

    public BackpackItem(Properties pProperties, int pRowAmount) {
        super(pProperties);
        this.rowAmount = pRowAmount;
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player pPlayer = pContext.getPlayer();
        InteractionHand pUsedHand = pContext.getHand();

        if (pPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return new TextComponent(pPlayer.getItemInHand(pUsedHand).getDisplayName().getString().replace("[", "").replace("]", ""));
                }

                @Override
                public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
                    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                    packetBuffer.writeBlockPos(pPlayer.blockPosition());
                    packetBuffer.writeByte(pUsedHand == InteractionHand.MAIN_HAND ? 0 : 1);
                    MenuType<ChestMenu> menu = menuType(getRowAmount());
                    return new BackpackMenu(menu, id, inventory, packetBuffer, rowAmount);
                }
            }, buf -> {
                buf.writeBlockPos(pPlayer.blockPosition());
                buf.writeByte(pUsedHand == InteractionHand.MAIN_HAND ? 0 : 1);
            });
        }
        return super.useOn(pContext);

    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {

        if (pPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return new TextComponent(pPlayer.getItemInHand(pUsedHand).getDisplayName().getString().replace("[", "").replace("]", ""));
                }

                @Override
                public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
                    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                    packetBuffer.writeBlockPos(pPlayer.blockPosition());
                    packetBuffer.writeByte(pUsedHand == InteractionHand.MAIN_HAND ? 0 : 1);
                    MenuType<ChestMenu> menu = menuType(getRowAmount());
                    return new BackpackMenu(menu, id, inventory, packetBuffer, rowAmount);
                }
            }, buf -> {
                buf.writeBlockPos(pPlayer.blockPosition());
                buf.writeByte(pUsedHand == InteractionHand.MAIN_HAND ? 0 : 1);
            });
        }
        return super.use(pLevel, pPlayer, pUsedHand);

    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag compound) {
        return new BackPackCapability(this.rowAmount);
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag nbt = super.getShareTag(stack);
        if (nbt != null)
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> nbt.put("Inventory", ((ItemStackHandler) capability).serializeNBT()));
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null)
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
                    .ifPresent(capability -> ((ItemStackHandler) capability).deserializeNBT((CompoundTag) nbt.get("Inventory")));
    }

    private MenuType<ChestMenu> menuType(int pRows) {
        if (pRows == 1) {
            return MenuType.GENERIC_9x1;
        } else if (pRows == 2) {
            return MenuType.GENERIC_9x2;
        } else if (pRows == 3) {
            return MenuType.GENERIC_9x3;
        } else if (pRows == 4) {
            return MenuType.GENERIC_9x4;
        } else if (pRows == 5) {
            return MenuType.GENERIC_9x5;
        } else {
            return MenuType.GENERIC_9x6;
        }
    }

    public int getRowAmount(){
        return this.rowAmount;
    }
}
