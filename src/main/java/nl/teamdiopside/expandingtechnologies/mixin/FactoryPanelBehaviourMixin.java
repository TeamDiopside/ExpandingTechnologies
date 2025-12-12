package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBehaviour;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import com.simibubi.create.content.logistics.packagerLink.LogisticsManager;
import com.simibubi.create.content.logistics.packagerLink.LogisticsNetwork;
import net.minecraft.world.level.Level;
import nl.teamdiopside.expandingtechnologies.behaviour.ITickTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FactoryPanelBehaviour.class, remap = false)
public abstract class FactoryPanelBehaviourMixin {

    @Shadow
    private int lastReportedUnloadedLinks;

    @Unique
    private int expandingtechnologies$lastReportedNotTickingLinks;

    @Redirect(method = "tickStorageMonitor", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/factoryBoard/FactoryPanelBehaviour;getLevelInStorage()I"))
    private int injected(FactoryPanelBehaviour instance) {
        int unloadedLinkCount = instance.getUnloadedLinks() + expandingtechnologies$getNotTickingLinks(instance);
        FactoryPanelBlockEntity panelBE = instance.panelBE();
        if (!panelBE.restocker && unloadedLinkCount == 0 && lastReportedUnloadedLinks != 0) {
            // All links have been loaded, invalidate cache so we can get an accurate summary!
            // Otherwise, we will have to wait for 20 ticks and unnecessary packages will be sent!
            LogisticsManager.SUMMARIES.invalidate(instance.network);
        }
        return instance.getLevelInStorage();
    }
    @Redirect(method = "tickStorageMonitor", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/factoryBoard/FactoryPanelBehaviour;getUnloadedLinks()I"))
    private int injected2(FactoryPanelBehaviour instance) {
        expandingtechnologies$lastReportedNotTickingLinks = expandingtechnologies$getNotTickingLinks(instance);
        // I know this is cursed, but it saves me a few redirects.
        // Unloaded and not ticking, almost the same right?
        return instance.getUnloadedLinks() + expandingtechnologies$lastReportedNotTickingLinks;
    }

    @Unique
    public int expandingtechnologies$getNotTickingLinks(FactoryPanelBehaviour instance) {
        Level level = instance.getWorld();
        if (level == null || level.isClientSide())
            return expandingtechnologies$lastReportedNotTickingLinks;
        if (instance.panelBE().restocker) {
            PackagerBlockEntity pbe = instance.panelBE().getRestockedPackager();
            return pbe == null ? 1 : 0;
        }

        LogisticsNetwork logisticsNetwork = Create.LOGISTICS.logisticsNetworks.get(instance.network);
        if (logisticsNetwork == null)
            return 0;
        return Math.toIntExact(logisticsNetwork.loadedLinks.stream()
                .filter(pos -> pos.dimension() == level.dimension()
                        && level.getBlockEntity(pos.pos()) instanceof ITickTracking plbe
                        && !plbe.expandingtechnologies$isTicking())
                .count());
    }
}
