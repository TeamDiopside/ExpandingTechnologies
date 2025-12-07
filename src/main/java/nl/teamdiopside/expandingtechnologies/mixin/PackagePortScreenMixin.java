package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.logistics.packagePort.PackagePortMenu;
import com.simibubi.create.content.logistics.packagePort.PackagePortScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import nl.teamdiopside.expandingtechnologies.registry.ETConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PackagePortScreen.class, remap = false)
public abstract class PackagePortScreenMixin extends AbstractSimiContainerScreen<PackagePortMenu> {
    public PackagePortScreenMixin(PackagePortMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Shadow
    private EditBox addressBox;

    @Inject(method = "renderBg", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/EditBox;isFocused()Z"
    ), remap = true)
    private void et$renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY, CallbackInfo ci) {
        if (!addressBox.isFocused() && addressBox.getValue().contains("@s") && ETConfigs.common().allowSelfAddress.get()) {
            addressBox.setValue(addressBox.getValue().replace("@s", this.getMenu().player.getName().getString()));
        }
    }

    @Inject(method = "removed", at = @At(value = "HEAD"), remap = true)
    private void et$removed(CallbackInfo ci) {
        addressBox.setValue(addressBox.getValue().replace(
                "@s", ETConfigs.common().allowSelfAddress.get() ? this.getMenu().player.getName().getString() : "@s"
        ));
    }
}
