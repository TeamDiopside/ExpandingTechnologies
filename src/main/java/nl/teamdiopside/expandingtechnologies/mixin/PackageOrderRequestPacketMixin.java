package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour;
import com.simibubi.create.content.logistics.redstoneRequester.RedstoneRequesterBlock;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderRequestPacket;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import nl.teamdiopside.expandingtechnologies.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {PackageOrderRequestPacket.class}, remap = false)
public class PackageOrderRequestPacketMixin {

    public PackageOrderRequestPacketMixin() {}

    @Shadow
    private String address;

    @Shadow
    private PackageOrderWithCrafts order;


    @Redirect(method = "applySettings(Lnet/minecraft/server/level/ServerPlayer;Lcom/simibubi/create/content/logistics/stockTicker/StockTickerBlockEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/redstoneRequester/RedstoneRequesterBlock;programRequester(Lnet/minecraft/server/level/ServerPlayer;Lcom/simibubi/create/content/logistics/stockTicker/StockTickerBlockEntity;Lcom/simibubi/create/content/logistics/stockTicker/PackageOrderWithCrafts;Ljava/lang/String;)V"
            )
    )
    private void injected(ServerPlayer player, StockTickerBlockEntity be, PackageOrderWithCrafts order, String address) {
        boolean shouldReplace = AllBlocks.REDSTONE_REQUESTER.isIn(player.getMainHandItem()) && Config.allowSelfAddress;
        RedstoneRequesterBlock.programRequester(player, be, order,
                address.replace("@s", shouldReplace ? player.getName().getString() : "@s")
        );
    }

    @Inject(
            method = "applySettings(Lnet/minecraft/server/level/ServerPlayer;Lcom/simibubi/create/content/logistics/stockTicker/StockTickerBlockEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/stockTicker/StockTickerBlockEntity;broadcastPackageRequest(Lcom/simibubi/create/content/logistics/packagerLink/LogisticallyLinkedBehaviour$RequestType;Lcom/simibubi/create/content/logistics/stockTicker/PackageOrderWithCrafts;Lcom/simibubi/create/content/logistics/packager/IdentifiedInventory;Ljava/lang/String;)Z"
            ),
            cancellable = true
    )
    private void et$applySettings(ServerPlayer player, StockTickerBlockEntity be, CallbackInfo ci) {
        if (Config.allowSelfAddress) {
            be.broadcastPackageRequest(LogisticallyLinkedBehaviour.RequestType.PLAYER, order, null,
                    address.replace("@s", player.getName().getString())
            );
            ((StockTickerBlockEntityAccessor)be).setPreviouslyUsedAddress(address);
            ci.cancel();
        }
    }
}
