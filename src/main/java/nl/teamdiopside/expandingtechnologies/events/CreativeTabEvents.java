package nl.teamdiopside.expandingtechnologies.events;

import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.registry.ETItems;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ExpandingTechnologies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabEvents {

    @SubscribeEvent
    public static void addItemsToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        Map<Item, Item> toAdd = ETItems.getMap();
        if (event.getTab() == AllCreativeModeTabs.BASE_CREATIVE_TAB.get()) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
            for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : entries) {
                stacks.add(entry.getKey());
            }
            for (ItemStack stack : stacks) {
                Item item = stack.getItem();
                while (toAdd.containsKey(item)) {
                    if (toAdd.get(item) == item) break;
                    ItemStack toAddStack = new ItemStack(toAdd.get(item));
                    entries.putAfter(stack, toAddStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    stack = toAddStack;
                    item = toAdd.get(item);
                }
            }
        }
    }
}
