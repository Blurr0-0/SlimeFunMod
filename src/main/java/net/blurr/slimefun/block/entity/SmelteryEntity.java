package net.blurr.slimefun.block.entity;

import io.netty.util.internal.ThreadLocalRandom;
import net.blurr.slimefun.item.ModItems;
import net.blurr.slimefun.recipe.AlloyingRecipe;
import net.blurr.slimefun.screen.OreWasherMenu;
import net.blurr.slimefun.screen.SmelteryMenu;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;

public class SmelteryEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private static int maxProgress;

    public SmelteryEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SMELTERY_ENTITY.get(), pWorldPosition, pBlockState);
        this.data = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0: return SmelteryEntity.this.progress;
                    case 1: return SmelteryEntity.this.maxProgress;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: SmelteryEntity.this.progress = value; break;
                    case 1: SmelteryEntity.this.maxProgress = value; break;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Smeltery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new SmelteryMenu(pContainerId, pInventory, this, this.data);
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
        tag.putInt("smeltery.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("smeltery.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SmelteryEntity pBlockEntity) {
        if(hasRecipe(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if(pBlockEntity.progress > pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);
            }
        } else {
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private static boolean hasRecipe(SmelteryEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<AlloyingRecipe> match = level.getRecipeManager()
                .getRecipeFor(AlloyingRecipe.Type.INSTANCE, inventory, level);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory, entity)
                && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem()) && hasCampfireBelow(entity);
    }
    private static boolean hasCampfireBelow(SmelteryEntity entity) {
        BlockPos pPos = entity.getBlockPos().below(1);
        BlockState pState = entity.getLevel().getBlockState(pPos);
        if (pState.getBlock() == Blocks.CAMPFIRE) {
            maxProgress = 200;
            return pState.getValue(CampfireBlock.LIT);
        } else if (pState.getBlock() == Blocks.SOUL_CAMPFIRE) {
            maxProgress = 100;
            return pState.getValue(CampfireBlock.LIT);
        }
        return false;
    }

    private static void craftItem(SmelteryEntity entity) {
        BlockPos pPos = entity.getBlockPos().below(1);
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<AlloyingRecipe> match = level.getRecipeManager()
                .getRecipeFor(AlloyingRecipe.Type.INSTANCE, inventory, level);

        if(match.isPresent()) {
            inventory.removeItem(1, 1);
            inventory.removeItem(2, 1);
            inventory.removeItem(3, 1);
            inventory.removeItem(4, 1);
            inventory.removeItem(5, 1);
            inventory.removeItem(6, 1);
            Random rand = new Random();


            entity.itemHandler.setStackInSlot(0, new ItemStack(match.get().getResultItem().getItem(),
                    entity.itemHandler.getStackInSlot(0).getCount() + match.get().getResultItem().getCount()));

            if (rand.nextInt(100) < 20) {
                BlockState pBlockState = level.getBlockState(pPos).cycle(CampfireBlock.LIT);
                level.setBlock(pPos, pBlockState, 3);
            }

            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(0).getItem() == output.getItem() || inventory.getItem(0).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, SmelteryEntity entity) {
        Level pLevel = entity.level;
        Optional<AlloyingRecipe> match = pLevel.getRecipeManager()
                .getRecipeFor(AlloyingRecipe.Type.INSTANCE, inventory, pLevel);
        return inventory.getItem(0).getMaxStackSize() >= inventory.getItem(0).getCount() + match.get().getResultItem().getCount();
    }

}
