package nl.teamdiopside.expandingtechnologies.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.decoration.slidingDoor.DoorControlBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.GlobalStation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nl.teamdiopside.expandingtechnologies.behaviour.IBetterContraptionBounds;
import nl.teamdiopside.expandingtechnologies.blocks.doorcontroller.DoorControllerBlock;
import nl.teamdiopside.expandingtechnologies.registry.ETConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlidingDoorMovementBehaviour.class, remap = false)
public abstract class SlidingDoorMovementBehaviourMixin {

    @Redirect(method = "getDoorFacing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;getCenter()Lnet/minecraft/world/phys/Vec3;"), remap = true)
    private Vec3 injected(AABB instance, @Local(argsOnly = true) MovementContext context) {
        if (ETConfigs.common().betterContraptionDoorPosition.get() && context.contraption instanceof IBetterContraptionBounds contraption) {
            if (contraption.expandingtechnologies$getBetterBounds() == null) {
                contraption.expandingtechnologies$calculateBetterBounds();
            }
            if (contraption.expandingtechnologies$getBetterBounds() != null) {
                return contraption.expandingtechnologies$getBetterBounds().getCenter();
            }
        }
        return instance.getCenter();
    }

    @Shadow protected abstract Direction getDoorFacing(MovementContext context);

    @Inject(method = "shouldOpenAt", at = @At(value = "HEAD"), cancellable = true)
    private void et$shouldOpenAt(DoorControlBehaviour controller, MovementContext context, CallbackInfoReturnable<Boolean> cir) {
        if (context.contraption.entity instanceof CarriageContraptionEntity cce && cce.getCarriage() != null) {
            Train train = cce.getCarriage().train;
            if (train == null || train.getCurrentStation() == null) return;

            GlobalStation station = train.getCurrentStation();
            BlockPos stationPos = station.getBlockEntityPos();
            MinecraftServer server = context.world.getServer();
            if (server == null) return;

            ServerLevel stationLevel = server.getLevel(station.getBlockEntityDimension());
            if (stationLevel == null || !stationLevel.isLoaded(stationPos)) return;

            BlockState doorControllerState = stationLevel.getBlockState(stationPos.below());
            if (doorControllerState.getBlock() instanceof DoorControllerBlock doorController) {
                cir.setReturnValue(doorController.shouldDoorOpen(this.getDoorFacing(context), doorControllerState));
            }
        }
    }

}
