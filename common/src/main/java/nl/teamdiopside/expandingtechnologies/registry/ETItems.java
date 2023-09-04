package nl.teamdiopside.expandingtechnologies.registry;

import com.google.common.base.Suppliers;
import com.simibubi.create.AllBlocks;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

import java.util.IdentityHashMap;
import java.util.Map;

public class ETItems {

    public static final Registrar<Item> REGISTRAR = Suppliers.memoize(() -> RegistrarManager.get(ExpandingTechnologies.MODID)).get().get(Registries.ITEM);

    public static final RegistrySupplier<Item> KINETIC_BATTERY = REGISTRAR.register(new ResourceLocation(ExpandingTechnologies.MODID, "kinetic_battery"), () -> new BlockItem(ETBlocks.KINETIC_BATTERY.get(), new Item.Properties()));

    private static final Map<Item, Item> TECHNOLOGIES_ITEMS = new IdentityHashMap<>();

    public static void add(ItemLike alreadyIn, ItemLike newStack) {
        TECHNOLOGIES_ITEMS.computeIfAbsent(alreadyIn.asItem(), $ -> newStack.asItem());
    }

    public static Map<Item, Item> getMap() {
        return TECHNOLOGIES_ITEMS;
    }

    public static void addToTab() {
        add(AllBlocks.TRACK_OBSERVER.get(), ETBlocks.RAILROAD_LIGHT_CONTROLLER.get());
        add(ETBlocks.RAILROAD_LIGHT_CONTROLLER.get(), KINETIC_BATTERY.get());
    }
}
