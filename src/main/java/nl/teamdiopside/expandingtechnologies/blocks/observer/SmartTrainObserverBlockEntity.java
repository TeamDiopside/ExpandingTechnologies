package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.observer.TrackObserver;
import com.simibubi.create.content.trains.observer.TrackObserverBlockEntity;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nl.teamdiopside.expandingtechnologies.gui.SmartTrainObserverMenu;
import nl.teamdiopside.expandingtechnologies.registry.ETEdgePointTypes;
import nl.teamdiopside.expandingtechnologies.registry.ETMenuTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmartTrainObserverBlockEntity extends TrackObserverBlockEntity implements MenuProvider {
    public TrackTargetingBehaviour<SmartTrainObserver> smartEdgePoint;

    protected ObserverCondition condition;

    public SmartTrainObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setCondition(ObserverCondition.getDefault());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.smartEdgePoint = new TrackTargetingBehaviour<>(this, ETEdgePointTypes.SMART_OBSERVER));
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
    }

    public ObserverCondition getCondition() {
        return condition;
    }

    public void setCondition(@NotNull ObserverCondition condition) {
        this.condition = condition;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        condition.toTag(tag);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        setCondition(ObserverCondition.fromTag(tag));
    }

    @Override
    public @Nullable TrackObserver getObserver() {
        return this.smartEdgePoint.getEdgePoint();
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return (new AABB(Vec3.atLowerCornerOf(this.worldPosition), Vec3.atLowerCornerOf(this.smartEdgePoint.getGlobalPosition()))).inflate(2.0);
    }

    @Override
    public void transform(BlockEntity be, StructureTransform transform) {
        this.smartEdgePoint.transform(be, transform);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getBlockState().getBlock().asItem().getDescription();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new SmartTrainObserverMenu(ETMenuTypes.SMART_TRAIN_OBSERVER_MENU.get(), i, inventory, new Pair<>(this.getBlockPos(), condition));
    }
}
