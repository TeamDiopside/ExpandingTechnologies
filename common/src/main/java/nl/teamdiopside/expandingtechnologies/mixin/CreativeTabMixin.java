package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.infrastructure.item.CreateCreativeModeTab;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nl.teamdiopside.expandingtechnologies.registry.ETItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = CreateCreativeModeTab.class, remap = false)
public abstract class CreativeTabMixin {

    @Inject(method = "addBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;fillItemCategory(Lnet/minecraft/world/item/CreativeModeTab;Lnet/minecraft/core/NonNullList;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void et$addBlocks(NonNullList<ItemStack> items, CallbackInfo ci, Iterator var2, RegistryEntry<Item> entry, BlockItem blockItem, Object var5) {
        ETItems.addToTab();
        Map<Item, Item> toAdd = ETItems.getMap();
        if (toAdd.containsKey(blockItem)) {
            items.add(new ItemStack(toAdd.get(blockItem)));
        }
    }
}
