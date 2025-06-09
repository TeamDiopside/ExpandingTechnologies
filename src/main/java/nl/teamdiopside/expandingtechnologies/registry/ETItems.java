package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.AllBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.IdentityHashMap;
import java.util.Map;

public class ETItems {

    private static final Map<Item, Item> TECHNOLOGIES_ITEMS = new IdentityHashMap<>();

    public static void add(ItemLike alreadyIn, ItemLike newStack) {
        TECHNOLOGIES_ITEMS.computeIfAbsent(alreadyIn.asItem(), $ -> newStack.asItem());
    }

    public static Map<Item, Item> getMap() {
        if (TECHNOLOGIES_ITEMS.isEmpty()) addToTab();
        return TECHNOLOGIES_ITEMS;
    }

    private static void addToTab() {
        add(AllBlocks.TRACK_OBSERVER.get(), ETBlocks.RAILROAD_LIGHT_CONTROLLER.get());
        add(ETBlocks.RAILROAD_LIGHT_CONTROLLER.get(), ETBlocks.DOOR_CONTROLLER.get());
    }
}
