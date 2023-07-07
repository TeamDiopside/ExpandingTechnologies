package nl.teamdiopside.expandingtechnologies.util.fabric;

import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;

public class ETUtilImpl {
    public static int nextTabId() {
        return ItemGroupUtil.expandArrayAndGetId();
    }
}
