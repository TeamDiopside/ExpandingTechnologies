package nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;

public class KineticBatteryBlockEntity extends GeneratingKineticBlockEntity {

    public int maxStress;

    public KineticBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        // get from tier
        maxStress = state.getBlock() == ETBlocks.KINETIC_BATTERY.get() ? 200 : 0;
    }
}
