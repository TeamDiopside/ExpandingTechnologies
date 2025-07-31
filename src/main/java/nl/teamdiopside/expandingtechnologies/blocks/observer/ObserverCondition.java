package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.simibubi.create.content.trains.entity.Train;
import net.createmod.catnip.data.Glob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import nl.teamdiopside.expandingtechnologies.registry.ETObserverConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ObserverCondition(@NotNull ETObserverConditions.ObserverConditionRegistryEntry entry, @NotNull String filter) {

    public ObserverCondition(@NotNull ETObserverConditions.ObserverConditionRegistryEntry entry, @Nullable String filter) {
        this.entry = entry;
        this.filter = filter == null ? "" : filter;
    }

    public boolean evaluate(@NotNull Train train) {
        return this.entry.getEvaluateFunction().apply(train, filter);
    }

    public static ObserverCondition getDefault() {
        return ETObserverConditions.DESTINATION_CONDITION.buildCondition("");
    }

    public static ObserverCondition fromBuf(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        String filter = buf.readUtf();
        ETObserverConditions.ObserverConditionRegistryEntry entry = ETObserverConditions.REGISTRY.getEntry(id);
        if (entry == null) return getDefault();
        return entry.buildCondition(filter);
    }

    public void toBuf(FriendlyByteBuf buf) {
        buf.writeUtf(this.entry.getId());
        buf.writeUtf(this.filter);
    }

    public static ObserverCondition fromTag(CompoundTag tag) {
        CompoundTag conditionTag = tag.getCompound("Condition");
        String id = conditionTag.getString("Id");
        String filter = conditionTag.getString("Filter");
        ETObserverConditions.ObserverConditionRegistryEntry entry = ETObserverConditions.REGISTRY.getEntry(id);
        if (entry == null) return getDefault();
        return entry.buildCondition(filter);
    }

    public void toTag(CompoundTag tag) {
        CompoundTag conditionTag = new CompoundTag();
        conditionTag.putString("Id", this.entry.getId());
        conditionTag.putString("Filter", this.filter);
        tag.put("Condition", conditionTag);
    }

    @Override
    public String toString() {
        return entry.getId() + ": " + filter;
    }
}
