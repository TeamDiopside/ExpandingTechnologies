package nl.teamdiopside.expandingtechnologies.util.forge;

import net.minecraft.world.item.CreativeModeTab;

public class ETUtilImpl {
    public static int nextTabId() {
        return CreativeModeTab.getGroupCountSafe();
    }
}
