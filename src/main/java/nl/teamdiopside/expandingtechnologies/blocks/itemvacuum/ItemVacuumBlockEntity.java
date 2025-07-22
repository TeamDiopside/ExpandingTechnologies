package nl.teamdiopside.expandingtechnologies.blocks.itemvacuum;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemVacuumBlockEntity extends KineticBlockEntity {

    public SmartInventory inventory;
    private FilteringBehaviour filtering;
    private final VersionedInventoryWrapper itemHandler;

    public ItemVacuumBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.inventory = new SmartInventory(10, this);
        this.itemHandler = new VersionedInventoryWrapper(this.inventory);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        float speed = Math.abs(this.getSpeed());
        if (this.getLevel() == null || (speed < 1 && speed >= 0)) return;

        int maxItems = Math.max((int)(speed / 40), 1);
        List<ItemEntity> allItems = this.getLevel().getEntitiesOfClass(ItemEntity.class,
                new AABB(this.getBlockPos()).inflate(speedToRange(speed))
        );
        int entityIndex = 0;
        int itemCount = 0;

        while (itemCount < maxItems && entityIndex < allItems.size()) {
            ItemEntity currentItem = allItems.get(entityIndex);
            if (!this.filtering.test(currentItem.getItem())) {
                entityIndex++;
                continue;
            }

            int validSlot = -1;
            for (int j = 0; j < inventory.getSlots(); j++) {
                if (canPlaceInSlot(j, currentItem.getItem().getItem())) {
                    validSlot = j;
                    break;
                }
            }

            if (validSlot != -1) {
                itemHandler.insertItem(validSlot, new ItemStack(currentItem.getItem().getItem(), 1), false);
                if (currentItem.getItem().getCount() > 1) {
                    currentItem.getItem().setCount(currentItem.getItem().getCount() - 1);
                } else {
                    currentItem.kill();
                    entityIndex++;
                }
                itemCount++;
            } else {
                break;
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
    }


    @Override
    public void destroy() {
        super.destroy();
        Level level = this.getLevel();
        if (level == null) return;

        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Block.popResource(level, this.getBlockPos(), stack);
            }
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        setLazyTickRate(Math.max(1, (int)(40 / Math.abs(this.getSpeed()))));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.filtering = (new FilteringBehaviour(this, new ValueBoxTransform.Sided() {
            @Override
            protected Vec3 getSouthLocation() {
                return VecHelper.voxelSpace(8.0, 2, 15.5);
            }

            @Override
            protected boolean isSideActive(BlockState state, Direction direction) {
                return direction.getAxis() != Direction.Axis.Y;
            }
        })));
    }

    public boolean canPlaceInSlot(int slot, Item itemToPut) {
        ItemStack stackInSlot = inventory.getStackInSlot(slot);
        return stackInSlot.isEmpty() || (stackInSlot.getCount() < inventory.getSlotLimit(slot) && stackInSlot.getItem() == itemToPut);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (isItemHandlerCap(cap)) {
            if (side == Direction.DOWN) {
                return LazyOptional.of(() -> itemHandler).cast();
            }
            String caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                    .walk(frames -> frames
                            .skip(2)
                            .findFirst()
                            .map(StackWalker.StackFrame::getClassName)
                            .orElse("Unknown")
                    );
            if (caller.contains("jade")) {
                return LazyOptional.of(() -> itemHandler).cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public int speedToRange(float speed) {
        // Range between 2 and 16
        speed = Mth.abs(speed);
        if (speed < 1) return 0;
        if (speed >= 256) return 16;
        return (int)(2 + (speed / 18.2857142857));
    }
}
