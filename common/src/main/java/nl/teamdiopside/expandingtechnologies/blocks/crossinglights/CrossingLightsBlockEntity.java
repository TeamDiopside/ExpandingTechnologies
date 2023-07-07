package nl.teamdiopside.expandingtechnologies.blocks.crossinglights;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.registry.ETSounds;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

import java.util.List;
import java.util.Objects;

import static nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock.STATE;

public class CrossingLightsBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    public CrossingLightsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(3);
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
    public void lazyTick() {
        if (Objects.requireNonNull(getLevel()).getBlockState(getBlockPos().relative(Direction.UP)).getBlock() instanceof NoteBlock && getBlockState().getValue(STATE) != 0) {
            getLevel().playSound(null, getBlockPos().relative(Direction.UP), ETSounds.CROSSING_BELL.get(), SoundSource.BLOCKS, 1, 1);
        }
    }

    @Override
    public void transform(StructureTransform transform) {

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
