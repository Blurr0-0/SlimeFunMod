package net.blurr.slimefun.block.entity;

import io.netty.util.internal.ThreadLocalRandom;
import net.blurr.slimefun.SlimeFun;
import net.blurr.slimefun.item.ModItems;
import net.blurr.slimefun.screen.OreWasherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class OreWasherEntity extends BlockEntity implements MenuProvider {

    private final @NotNull ItemStack[] dusts = new ItemStack[] {
            new ItemStack(ModItems.IRON_DUST.get()),
            new ItemStack(ModItems.GOLD_DUST.get()),
            new ItemStack(ModItems.COPPER_DUST.get()),
            new ItemStack(ModItems.TIN_DUST.get()),
            new ItemStack(ModItems.LEAD_DUST.get()),
            new ItemStack(ModItems.SILVER_DUST.get()),
            new ItemStack(ModItems.ALUMINUM_DUST.get()),
            new ItemStack(ModItems.ZINC_DUST.get()),
            new ItemStack(ModItems.MAGNESIUM_DUST.get()),
            new ItemStack(ModItems.STONE_CHUNK.get())
    };



    private final ItemStackHandler itemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public OreWasherEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.ORE_WASHER_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Ore Washing");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new OreWasherMenu(pContainerId, pInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public @Nonnull ItemStack getRandomDust() {
        int index = ThreadLocalRandom.current().nextInt(dusts.length);
        return dusts[index].copy();
    }

    public boolean isItemInInv(OreWasherEntity pBlockEntity, Item pItem) {

        for (int slot = 0; slot <= 8; slot++) {
            if (pBlockEntity.itemHandler.getStackInSlot(slot).getItem() == pItem && pBlockEntity.itemHandler.getStackInSlot(slot).getCount() < pBlockEntity.itemHandler.getStackInSlot(slot).getItem().getMaxStackSize()) {
                return true;

            }
        }

        return false;
    }

    public int getSlotItemIn(OreWasherEntity pBlockEntity, Item pItem) {

        for (int slot = 0; slot <= 8; slot++) {
            if (pBlockEntity.itemHandler.getStackInSlot(slot).getItem() == pItem && pBlockEntity.itemHandler.getStackInSlot(slot).getCount() < pBlockEntity.itemHandler.getStackInSlot(slot).getItem().getMaxStackSize()) {
                return slot;
            }
        }

        return -1;
    }

    public int getFreeSlot(OreWasherEntity pBlockEntity) {

        for (int slot = 0; slot <= 8; slot++) {
            if (pBlockEntity.itemHandler.getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }

        return -1;
    }

    private static ItemStack currentDust;
    private static Item previousDust;

    public static void craft(Level pLevel, BlockPos pPos, BlockState pState, OreWasherEntity pBlockEntity, Player pPlayer) {
        setChanged(pLevel, pPos, pState);
        ItemStack siftedOre = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStackHandler handler = pBlockEntity.itemHandler;
        if (previousDust == null)
        {
            currentDust = pBlockEntity.getRandomDust();
            previousDust = currentDust.getItem();
        }


        if (pPlayer.isHolding(ModItems.SIFTED_ORE.get())) {
            if (pBlockEntity.isItemInInv(pBlockEntity, previousDust) && handler.getStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, previousDust)).getCount() < 64 && previousDust == currentDust.getItem()) {
                pPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(siftedOre.getItem(), siftedOre.getCount() - 1));
                handler.setStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, previousDust), new ItemStack(previousDust, handler.getStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, previousDust)).getCount() + 1));
                currentDust = pBlockEntity.getRandomDust();
                previousDust = currentDust.getItem();
                pLevel.playSound(null, pPos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);
            } else if (pBlockEntity.getFreeSlot(pBlockEntity) != -1) {
                pPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(siftedOre.getItem(), siftedOre.getCount() - 1));
                handler.setStackInSlot(pBlockEntity.getFreeSlot(pBlockEntity), currentDust);
                currentDust = pBlockEntity.getRandomDust();
                previousDust = currentDust.getItem();
                pLevel.playSound(null, pPos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);
            } else {
                pPlayer.sendMessage(new TranslatableComponent("machines.full-inventory"), pPlayer.getUUID());
                setChanged(pLevel, pPos, pState);
            }
        } else if (pPlayer.isHolding(Blocks.SAND.asItem()) && pPlayer.getMainHandItem().getCount() >= 2) {
            ItemStack salt = new ItemStack(ModItems.SALT.get(), 1);
            if (pBlockEntity.isItemInInv(pBlockEntity, salt.getItem()) && handler.getStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, salt.getItem())).getCount() < 64) {
                pPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Blocks.SAND.asItem(), pPlayer.getMainHandItem().getCount() - 2));
                handler.setStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, salt.getItem()), new ItemStack(salt.getItem(), handler.getStackInSlot(pBlockEntity.getSlotItemIn(pBlockEntity, salt.getItem())).getCount() + 1));
                pLevel.playSound(null, pPos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);
            } else if (pBlockEntity.getFreeSlot(pBlockEntity) != -1) {
                pPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Blocks.SAND.asItem(), pPlayer.getMainHandItem().getCount() - 2));
                handler.setStackInSlot(pBlockEntity.getFreeSlot(pBlockEntity), salt);
                pLevel.playSound(null, pPos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 1, 1);
            } else {
                pPlayer.sendMessage(new TranslatableComponent("machines.full-inventory"), pPlayer.getUUID());
                setChanged(pLevel, pPos, pState);
            }
        }
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, OreWasherEntity pBlockEntity) {
    }

}
