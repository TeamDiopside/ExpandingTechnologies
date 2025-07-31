package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.MenuEntry;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.gui.SmartTrainObserverMenu;
import nl.teamdiopside.expandingtechnologies.gui.SmartTrainObserverScreen;

public class ETMenuTypes {
    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

    public static final MenuEntry<SmartTrainObserverMenu> SMART_TRAIN_OBSERVER_MENU = REGISTRATE.menu(
            "smart_train_observer_menu",
            SmartTrainObserverMenu::new,
            () -> SmartTrainObserverScreen::new
    ).register();

    public static void register() {}
}
