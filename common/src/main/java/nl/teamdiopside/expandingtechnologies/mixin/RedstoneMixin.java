package nl.teamdiopside.expandingtechnologies.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.util.RedstoneConnectable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {RedStoneWireBlock.class})
public class RedstoneMixin {
    // Worthless half-working Mixin because Fabric doesn't have basic methods
    @Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void et$shouldConnectTo(BlockState state, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof RedstoneConnectable con) {
            cir.setReturnValue(con.canConnectRedstone(state, null, null, side));
        }
    }
}
