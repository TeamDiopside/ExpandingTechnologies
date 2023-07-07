package nl.curryducker.expandingtechnologies.blocks.crossinglights;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static nl.curryducker.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock.STATE;

public class CrossingLightsBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    public CrossingLightsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MutableComponent getStateForDisplay() {
        switch (getBlockState().getValue(STATE)) {
            case 0 -> {
                return Components.literal("§§");
            }
            case 1 -> {
                return Components.literal("x§");
            }
            case 2 -> {
                return Components.literal("§x");
            }
        }
        return Components.literal("§§");
    }

    @Override
    public void transform(StructureTransform transform) {

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
