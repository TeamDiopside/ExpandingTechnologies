package nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;

import java.util.List;

public class KineticBatteryBlockEntity extends GeneratingKineticBlockEntity {

    public int maxStress;
    public boolean charging;
    public ScrollValueBehaviour generatedSpeed;

    public KineticBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        // get max stress from tier
        maxStress = state.getBlock() == ETBlocks.KINETIC_BATTERY.get() ? 200 : 0;
        charging = state.getValue(KineticBatteryBlock.CHARGING);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = CreativeMotorBlockEntity.MAX_SPEED;
        generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.creative_motor.rotation_speed"), this, new SpeedBox());
        generatedSpeed.between(-max, max);
        generatedSpeed.value = CreativeMotorBlockEntity.DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        behaviours.add(generatedSpeed);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!AllBlocks.CREATIVE_MOTOR.has(getBlockState()))
            return 0;
        return convertToDirection(generatedSpeed.getValue(), getBlockState().getValue(KineticBatteryBlock.FACING));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (charging) {
            return false;
        }
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    static class SpeedBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = state.getValue(KineticBatteryBlock.FACING);
            return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            super.rotate(state, ms);
            Direction facing = state.getValue(KineticBatteryBlock.FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.cast(ms)
                    .rotateZ(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(KineticBatteryBlock.FACING);
            boolean charging = state.getValue(KineticBatteryBlock.CHARGING);
            if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN || charging)
                return false;
            return direction.getAxis() != facing.getAxis();
        }
    }
}
