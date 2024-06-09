package net.blurr.slimefun.block.entity;

import net.blurr.slimefun.screen.BlockPlacerMenu;
import net.blurr.slimefun.screen.OreWasherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPlacerEntity extends RandomizableContainerBlockEntity implements MenuProvider {

    private static final Random RANDOM = new Random();
    public static final int CONTAINER_SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    private final ItemStackHandler itemHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    public BlockPlacerEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.BLOCK_PLACER_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    public int getRandomSlot() {
        this.unpackLootTable((Player)null);
        int i = -1;
        int j = 1;

        for(int k = 0; k < this.items.size(); ++k) {
            if ((this.items.get(k).getItem() instanceof BlockItem) && RANDOM.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    public int addItem(ItemStack pStack) {
        for(int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).isEmpty()) {
                this.setItem(i, pStack);
                return i;
            }
        }

        return -1;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.dispenser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new BlockPlacerMenu(pContainerId, pInventory, this);
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
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(pTag)) {
            ContainerHelper.loadAllItems(pTag, this.items);
        }

    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.trySaveLootTable(pTag)) {
            ContainerHelper.saveAllItems(pTag, this.items);
        }

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }
}
