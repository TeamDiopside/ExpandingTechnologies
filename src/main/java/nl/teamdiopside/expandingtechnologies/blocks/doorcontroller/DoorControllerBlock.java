package nl.teamdiopside.expandingtechnologies.blocks.doorcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import nl.teamdiopside.expandingtechnologies.util.RedstoneConnectable;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class DoorControllerBlock extends Block implements RedstoneConnectable {

    public DoorControllerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
        );
    }

    @Override
    @Deprecated
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbor, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, neighbor, movedByPiston);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos redstone = pos.relative(dir);
            int power = level.getSignal(redstone, dir);
            BooleanProperty booleanProperty = null;
            switch (dir) {
                case NORTH -> booleanProperty = NORTH;
                case EAST -> booleanProperty = EAST;
                case SOUTH -> booleanProperty = SOUTH;
                case WEST -> booleanProperty = WEST;
            }
            if (booleanProperty != null) {
                state = state.setValue(booleanProperty, power > 0);
                level.setBlock(pos, state, 3);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return true;
    }

    public boolean shouldDoorOpen(Direction doorFacing, BlockState doorControllerState) {
        switch (doorFacing) {
            case NORTH -> { return doorControllerState.getValue(NORTH); }
            case EAST -> { return doorControllerState.getValue(EAST); }
            case SOUTH -> { return doorControllerState.getValue(SOUTH); }
            case WEST -> { return doorControllerState.getValue(WEST); }
            default -> { return false; }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }
}
