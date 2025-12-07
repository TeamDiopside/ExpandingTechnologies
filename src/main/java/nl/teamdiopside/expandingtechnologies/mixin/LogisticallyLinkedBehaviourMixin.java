package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LogisticallyLinkedBehaviour.class, remap = false)
public abstract class LogisticallyLinkedBehaviourMixin extends BlockEntityBehaviour {

    @Shadow
    public abstract void lazyTick();

    public LogisticallyLinkedBehaviourMixin(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "initialize", at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/logistics/packagerLink/GlobalLogisticsManager;linkLoaded(Ljava/util/UUID;Lnet/minecraft/core/GlobalPos;)V",
            shift = At.Shift.AFTER
    ))
    private void et$initialize(CallbackInfo ci) {
        // Call keepAlive regardless of redstone power.
        // Otherwise, when no redstone power is present
        // keepAlive won't be called until next lazy tick.
        lazyTick();
    }
}
