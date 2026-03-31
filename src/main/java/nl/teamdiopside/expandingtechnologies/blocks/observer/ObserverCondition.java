package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import nl.teamdiopside.expandingtechnologies.registry.ETObserverConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ObserverCondition(@NotNull ETObserverConditions.ObserverConditionRegistryEntry entry, @NotNull String filter, boolean inverted) {

    public static final StreamCodec<FriendlyByteBuf, ObserverCondition> STREAM_CODEC  = new StreamCodec<>() {
        public @NotNull ObserverCondition decode(@NotNull FriendlyByteBuf byteBuf) {
            return fromBuf(byteBuf);
        }

        public void encode(@NotNull FriendlyByteBuf byteBuf, ObserverCondition condition) {
            condition.toBuf(byteBuf);
        }
    };

    public ObserverCondition(@NotNull ETObserverConditions.ObserverConditionRegistryEntry entry, @Nullable String filter, boolean inverted) {
        this.entry = entry;
        this.filter = filter == null ? "" : filter;
        this.inverted = inverted;
    }

    public boolean evaluate(@NotNull Train train) {
        return inverted != this.entry.getEvaluateFunction().apply(train, filter);
    }

    public static ObserverCondition getDefault() {
        return ETObserverConditions.DESTINATION_CONDITION.buildCondition("", false);
    }

    public static @NotNull ObserverCondition fromBuf(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        String filter = buf.readUtf();
        boolean inverted = buf.readBoolean();
        ETObserverConditions.ObserverConditionRegistryEntry entry = ETObserverConditions.REGISTRY.getEntry(id);
        if (entry == null) return getDefault();
        return entry.buildCondition(filter, inverted);
    }

    public void toBuf(FriendlyByteBuf buf) {
        buf.writeUtf(this.entry.getId());
        buf.writeUtf(this.filter);
        buf.writeBoolean(this.inverted);
    }

    public static ObserverCondition fromTag(CompoundTag tag) {
        CompoundTag conditionTag = tag.getCompound("Condition");
        String id = conditionTag.getString("Id");
        String filter = conditionTag.getString("Filter");
        boolean inverted = conditionTag.getBoolean("Inverted");
        ETObserverConditions.ObserverConditionRegistryEntry entry = ETObserverConditions.REGISTRY.getEntry(id);
        if (entry == null) return getDefault();
        return entry.buildCondition(filter, inverted);
    }

    public void toTag(CompoundTag tag) {
        CompoundTag conditionTag = new CompoundTag();
        conditionTag.putString("Id", this.entry.getId());
        conditionTag.putString("Filter", this.filter);
        conditionTag.putBoolean("Inverted", this.inverted);
        tag.put("Condition", conditionTag);
    }

    @Override
    public String toString() {
        return entry.getId() + ": " + filter;
    }
}
