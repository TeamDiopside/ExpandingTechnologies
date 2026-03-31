package nl.teamdiopside.expandingtechnologies.events;

import com.simibubi.create.AllCreativeModeTabs;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import nl.teamdiopside.expandingtechnologies.registry.ETItems;

import java.util.ArrayList;
import java.util.Map;

@EventBusSubscriber
public class CreativeTabEvents {

    @SubscribeEvent
    public static void addItemsToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        Map<Item, Item> toAdd = ETItems.getMap();
        if (event.getTab() == AllCreativeModeTabs.BASE_CREATIVE_TAB.get()) {
            ObjectSortedSet<ItemStack> entries = event.getParentEntries();
            ArrayList<ItemStack> stacks = new ArrayList<>(entries);
            for (ItemStack stack : stacks) {
                Item item = stack.getItem();
                while (toAdd.containsKey(item)) {
                    if (toAdd.get(item) == item) break;
                    ItemStack toAddStack = new ItemStack(toAdd.get(item));
                    event.insertAfter(stack, toAddStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    stack = toAddStack;
                    item = toAdd.get(item);
                }
            }
        }
    }
}
