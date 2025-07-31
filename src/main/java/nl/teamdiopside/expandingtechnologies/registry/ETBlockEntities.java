package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlockEntity;
import nl.teamdiopside.expandingtechnologies.blocks.itemvacuum.ItemVacuumBlockEntity;
import nl.teamdiopside.expandingtechnologies.blocks.itemvacuum.ItemVacuumRenderer;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserverRenderer;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserverBlockEntity;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserverVisual;

public class ETBlockEntities {
    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

    public static final BlockEntityEntry<CrossingLightsBlockEntity> RAILROAD_LIGHT_CONTROLLER = REGISTRATE
            .blockEntity("railroad_light_controller", CrossingLightsBlockEntity::new)
            .validBlocks(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
            .register();

    public static final BlockEntityEntry<ItemVacuumBlockEntity> ITEM_VACUUM = REGISTRATE
            .blockEntity("item_vacuum", ItemVacuumBlockEntity::new)
            .validBlocks(ETBlocks.ITEM_VACUUM)
            .renderer(() -> ItemVacuumRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartTrainObserverBlockEntity> SMART_TRAIN_OBSERVER = REGISTRATE
            .blockEntity("smart_train_observer", SmartTrainObserverBlockEntity::new)
            .visual(() -> SmartTrainObserverVisual::new)
            .validBlocks(ETBlocks.SMART_TRAIN_OBSERVER)
            .renderer(() -> SmartTrainObserverRenderer::new)
            .register();

    public static void register() {}
}
