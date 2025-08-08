package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.display.GlobalTrainDisplayData;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.station.GlobalStation;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Glob;
import net.createmod.catnip.data.IntAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.observer.ObserverCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public class ETObserverConditions {

    public static ObserverConditionRegistry REGISTRY = new ObserverConditionRegistry();

    public static ObserverConditionRegistryEntry DESTINATION_CONDITION = REGISTRY.buildEntry("destination", (train, filter) -> {
        if (train.navigation.destination == null) return false;
        return stringMatchesRegex(train.navigation.destination.name, filter);
    }).icon(AllBlocks.TRACK_STATION).suggestionFunction((blockPos, player) -> {
        HashSet<GlobalStation> stations = new HashSet<>();
        Create.RAILWAYS.sided(player.level()).trackNetworks.forEach((uuid, trackGraph) -> stations.addAll(trackGraph.getPoints(EdgePointType.STATION)));
        return stations.stream().map(station -> IntAttached.with((int)station.getBlockEntityPos().getCenter().distanceTo(blockPos.getCenter()), station.name)).toList();
    }).register();

    public static ObserverConditionRegistryEntry MANUAL_CONTROL_CONDITION = REGISTRY.buildEntry("manual_control", (train, filter) -> {
        for (Carriage carriage : train.carriages) {
            for (ResourceKey<Level> dimension : carriage.getPresentDimensions()) {
                CarriageContraptionEntity entity = carriage.getDimensional(dimension).entity.get();
                if (entity == null || entity.getControllingPlayer().isEmpty()) continue;
                Player player = entity.level().getPlayerByUUID(entity.getControllingPlayer().get());
                if (player == null) continue;
                if (stringMatchesRegex(player.getName().getString(), filter)) return true;
            }
        }
        return false;
    }).icon(AllBlocks.TRAIN_CONTROLS).suggestionFunction((blockPos, player) -> {
        Level level = player.level();
        return level.players().stream().map(player1 -> IntAttached.with((int)player1.getOnPos().getCenter().distanceTo(blockPos.getCenter()), player1.getName().getString())).toList();
    }).register();

    public static ObserverConditionRegistryEntry ENTITY_CONTROL_CONDITION = REGISTRY.buildEntry("entity_control", (train, filter) -> {
        if (train.runtime.schedule == null) return false;
        for (Carriage carriage : train.carriages) {
            for (ResourceKey<Level> dimension : carriage.getPresentDimensions()) {
                CarriageContraptionEntity entity = carriage.getDimensional(dimension).entity.get();
                if (entity == null) continue;
                if (!(entity.getContraption() instanceof CarriageContraption contraption)) continue;
                if (entity.getControllingPlayer().isPresent()) continue;

                for (Entity passenger : entity.getPassengers()) {
                    if (passenger instanceof Player) continue;
                    if (!entityMatchesFilter(passenger, filter)) continue;
                    if (isConductor(passenger, contraption)) return true;
                }
            }
        }
        return false;
    }).icon(AllBlocks.TRAIN_CONTROLS).suggestionFunction((blockPos, player) -> {
        Collection<Train> trains = Create.RAILWAYS.sided(player.level()).trains.values();
        List<IntAttached<String>> suggestions = new ArrayList<>();
        for (Train train : trains) {
            for (Carriage carriage : train.carriages) {
                CarriageContraptionEntity entity = carriage.getDimensional(player.level()).entity.get();
                if (entity == null) continue;
                if (!(entity.getContraption() instanceof CarriageContraption contraption)) continue;

                for (Entity passenger : entity.getPassengers()) {
                    if (passenger instanceof Player || passenger.getCustomName() == null) continue;
                    if (isConductor(passenger, contraption)) suggestions.add(IntAttached.with((int)entity.getPosition(0).distanceTo(blockPos.getCenter()), passenger.getCustomName().getString()));
                }
            }
        }
        return suggestions;
    }).register();

    public static ObserverConditionRegistryEntry PASSENGER_CONDITION = REGISTRY.buildEntry("has_passenger", (train, filter) -> {
        for (Carriage carriage : train.carriages) {
            for (ResourceKey<Level> dimension : carriage.getPresentDimensions()) {
                CarriageContraptionEntity entity = carriage.getDimensional(dimension).entity.get();
                if (entity == null) continue;
                if (!(entity.getContraption() instanceof CarriageContraption contraption)) continue;

                for (Entity passenger : entity.getPassengers()) {
                    if (!entityMatchesFilter(passenger, filter)) continue;
                    if (!isConductor(passenger, contraption)) return true;
                }
            }
        }
        return false;
    }).icon(AllBlocks.SEATS.get(DyeColor.RED)).suggestionFunction((blockPos, player) -> {
        Collection<Train> trains = Create.RAILWAYS.sided(player.level()).trains.values();
        List<IntAttached<String>> suggestions = new ArrayList<>();
        for (Train train : trains) {
            for (Carriage carriage : train.carriages) {
                CarriageContraptionEntity entity = carriage.getDimensional(player.level()).entity.get();
                if (entity == null) continue;
                if (!(entity.getContraption() instanceof CarriageContraption contraption)) continue;

                for (Entity passenger : entity.getPassengers()) {
                    if (isConductor(passenger, contraption)) continue;
                    if (passenger.getCustomName() == null && !(passenger instanceof Player)) continue;
                    suggestions.add(IntAttached.with((int)entity.getPosition(0).distanceTo(blockPos.getCenter()), passenger.getName().getString()));
                }
            }
        }
        return suggestions;
    }).register();

    public static ObserverConditionRegistryEntry STOPS_AT_STATION_CONDITION = REGISTRY.buildEntry("stops_at_station", (train, filter) -> {
        // If the station is our next destination we can stop looking.
        // This way we also support a bit of package delivery, provided the station is the current destination.
        ObserverCondition destinationCondition = DESTINATION_CONDITION.buildCondition(filter, false);
        if (destinationCondition.evaluate(train)) return true;

        // Using pre-calculated prediction data is the most efficient and accurate way.
        // Only downside is the fact that wildcard stations will only match if it's their current destination as routes are calculated per stop.
        for (GlobalTrainDisplayData.TrainDeparturePrediction prediction : GlobalTrainDisplayData.prepare(filter, Integer.MAX_VALUE)) {
            if (prediction.train == train) return true;
        }
        return false;
    }).icon(AllBlocks.TRACK_STATION).suggestionFunction((blockPos, player) -> {
        HashSet<GlobalStation> stations = new HashSet<>();
        Create.RAILWAYS.sided(player.level()).trackNetworks.forEach((uuid, trackGraph) -> stations.addAll(trackGraph.getPoints(EdgePointType.STATION)));
        return stations.stream().map(station -> IntAttached.with((int)station.getBlockEntityPos().getCenter().distanceTo(blockPos.getCenter()), station.name)).toList();
    }).register();

    public static ObserverConditionRegistryEntry TRAIN_NAME_CONDITION = REGISTRY
            .buildEntry("train_name", (train, filter) -> stringMatchesRegex(train.name.getString(), filter))
            .icon(Items.NAME_TAG)
            .suggestionFunction((blockPos, player) -> {
                Collection<Train> trains = Create.RAILWAYS.sided(player.level()).trains.values();
                List<String> names = trains.stream().map(train -> train.name.getString()).toList();
                return names.stream().sorted().map(name -> IntAttached.with(names.indexOf(name), name)).toList();
            }).register();

    public static void register() {
        ExpandingTechnologies.LOGGER.info("Registering Observer Conditions for ET");
    }



    public static class ObserverConditionEntryBuilder {
        private final @NotNull String id;
        private final @NotNull BiFunction<Train, String, Boolean> evaluateFunction;
        private @Nullable ItemLike icon;
        private @Nullable BiFunction<BlockPos, Player, List<IntAttached<String>>> suggestionFunction;

        private final @NotNull ObserverConditionRegistry registry;

        ObserverConditionEntryBuilder(@NotNull String id, @NotNull BiFunction<Train, String, Boolean> evaluateFunction, @NotNull ObserverConditionRegistry registry) {
            this.id = id;
            this.evaluateFunction = evaluateFunction;
            this.registry = registry;
        }

        public ObserverConditionEntryBuilder icon(ItemLike icon) {
            this.icon = icon;
            return this;
        }

        public ObserverConditionEntryBuilder suggestionFunction(BiFunction<BlockPos, Player, List<IntAttached<String>>> suggestionFunction) {
            this.suggestionFunction = suggestionFunction;
            return this;
        }

        public ObserverConditionRegistryEntry register() {
            String translationKey = "expandingtechnologies.observer_condition." + id;
            return registry.register(this.id, Component.translatable(translationKey), icon, evaluateFunction, suggestionFunction);
        }

    }

    public static class ObserverConditionRegistryEntry {
        private final @NotNull String id;
        private final @NotNull Component title;
        private final @Nullable ItemLike icon;
        private final @NotNull BiFunction<Train, String, Boolean> evaluateFunction;
        private final @NotNull BiFunction<BlockPos, Player, List<IntAttached<String>>> suggestionFunction;

        ObserverConditionRegistryEntry(@NotNull String id, @Nullable Component title, @Nullable ItemLike icon, @NotNull BiFunction<Train, String, Boolean> evaluateFunction, @Nullable BiFunction<BlockPos, Player, List<IntAttached<String>>> suggestionFunction) {
            this.id = id;
            this.title = title == null ? Component.empty() : title;
            this.icon = icon;
            this.evaluateFunction = evaluateFunction;
            this.suggestionFunction = suggestionFunction == null ? (blockPos, player) -> new ArrayList<>() : suggestionFunction;
        }

        public @NotNull String getId() {
            return id;
        }

        public @NotNull Component getTitle() {
            return title;
        }

        public @NotNull Item getIcon() {
            return icon == null ? Items.AIR : icon.asItem();
        }

        public @NotNull BiFunction<Train, String, Boolean> getEvaluateFunction() {
            return evaluateFunction;
        }

        public @NotNull List<IntAttached<String>> getSuggestions(BlockPos blockPos, Player player) {
            List<IntAttached<String>> allSuggestions = suggestionFunction.apply(blockPos, player);
            HashMap<String, Integer> allStrings = new HashMap<>();

            for (IntAttached<String> intAttached : allSuggestions) {
                int currentInt = intAttached.getFirst();
                String currentString = intAttached.getSecond();
                if (!allStrings.containsKey(currentString)) {
                    allStrings.put(currentString, currentInt);
                    continue;
                }

                // Remove duplicate strings, keep the lowest int
                if (allStrings.get(currentString) > currentInt) {
                    allStrings.put(currentString, currentInt);
                }
            }
            return allStrings.entrySet().stream().map(entry -> IntAttached.with(entry.getValue(), entry.getKey())).toList();
        }

        public ObserverCondition buildCondition(String filter, boolean inverted) {
            return new ObserverCondition(this, filter, inverted);
        }
    }

    public static class ObserverConditionRegistry {

        private final HashSet<ObserverConditionRegistryEntry> entries = new HashSet<>();

        ObserverConditionRegistry() {}

        ObserverConditionRegistryEntry register(@NotNull String id, @Nullable Component title, @Nullable ItemLike icon, @NotNull BiFunction<Train, String, Boolean> evaluateFunction, @Nullable BiFunction<BlockPos, Player, List<IntAttached<String>>> suggestionFunction) {
            ObserverConditionRegistryEntry entry = new ObserverConditionRegistryEntry(id, title, icon, evaluateFunction, suggestionFunction);
            entries.add(entry);
            return entry;
        }

        public ObserverConditionRegistryEntry getEntry(String id) {
            for (ObserverConditionRegistryEntry entry : entries) {
                if (entry.getId().equals(id)) return entry;
            }
            return null;
        }

        public ObserverConditionEntryBuilder buildEntry(@NotNull String id, @NotNull BiFunction<Train, String, Boolean> evaluateFunction) {
            return new ObserverConditionEntryBuilder(id, evaluateFunction, this);
        }

        public ArrayList<ObserverConditionRegistryEntry> getEntriesArray() {
            return new ArrayList<>(entries);
        }
    }

    /**
     * Condition functions
     */

    public static boolean isConductor(Entity entity, CarriageContraption contraption) {
        if (entity instanceof Player player) {
            Optional<UUID> controllingPlayer = contraption.entity.getControllingPlayer();
            return controllingPlayer.filter(uuid -> player.getUUID().equals(uuid)).isPresent();
        }
        BlockPos seatOf = contraption.getSeatOf(entity.getUUID());
        if (seatOf == null) return false;
        Couple<Boolean> validSides = contraption.conductorSeats.get(seatOf);
        return validSides != null && (validSides.getFirst() || validSides.getSecond());
    }

    @SuppressWarnings("all")
    public static Component getId(Entity entity) {
        return Component.literal(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    @SuppressWarnings("all")
    public static boolean entityMatchesFilter(Entity entity, String filter) {
        String entityId = getId(entity).getString();
        Component customName = entity.getCustomName();
        return stringMatchesRegex(entityId, filter) || (customName != null && stringMatchesRegex(customName.getString(), filter) || (entity instanceof Player player && stringMatchesRegex(player.getName().getString(), filter)));
    }

    public static boolean stringMatchesRegex(String string, String filter) {
        return string.matches(Glob.toRegexPattern(filter, ""));
    }
}
