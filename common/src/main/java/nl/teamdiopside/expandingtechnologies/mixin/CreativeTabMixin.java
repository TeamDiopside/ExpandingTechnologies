package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.teamdiopside.expandingtechnologies.registry.ETItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mixin(value = AllCreativeModeTabs.RegistrateDisplayItemsGenerator.class)
public abstract class CreativeTabMixin {
    public CreativeTabMixin() {}

    @Inject(method = "outputAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab$Output;accept(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void et$outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc, CallbackInfo ci, Iterator var4, Item item) {
        ETItems.addToTab();
        Map<Item, Item> toAdd = ETItems.getMap();
        if (toAdd.containsKey(item)) {
            output.accept(new ItemStack(toAdd.get(item)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
