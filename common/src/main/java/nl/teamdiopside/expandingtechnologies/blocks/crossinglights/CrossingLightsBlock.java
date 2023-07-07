package nl.teamdiopside.expandingtechnologies.blocks.crossinglights;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import nl.teamdiopside.expandingtechnologies.util.RedstoneConnectable;

import java.util.Objects;

public class CrossingLightsBlock extends HorizontalDirectionalBlock implements IBE<CrossingLightsBlockEntity>, IWrenchable, RedstoneConnectable {

    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 2);

    public CrossingLightsBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(STATE, 0).setValue(FACING, Direction.NORTH));
    }

    public static int lightTimings() {
        return 10;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        switch (pState.getValue(STATE)) {
            case 0 -> {
                for (Direction d : Iterate.directions) {
                    if (pLevel.getBlockState(pPos.relative(d)).getBlock() instanceof DisplayLinkBlock) {
                        pLevel.updateNeighborsAt(pPos.relative(d), this);
                        if (!pLevel.isClientSide)
                            ((DisplayLinkBlockEntity) Objects.requireNonNull(pLevel.getBlockEntity(pPos.relative(d)))).updateGatheredData();
                    }
                }
            }
            case 1 -> {
                pLevel.setBlock(pPos, pState.setValue(STATE, 2), 3);
                pLevel.scheduleTick(pPos, this, lightTimings());
            }
            case 2 -> {
                pLevel.setBlock(pPos, pState.setValue(STATE, 1), 3);
                pLevel.scheduleTick(pPos, this, lightTimings());
            }
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.isClientSide)
            return;
        if (pState.getValue(STATE) == 0 && pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.setValue(STATE, 1), 3);
            pLevel.scheduleTick(new BlockPos(pPos), this, lightTimings());
        } else if (pState.getValue(STATE) != 0 && !pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.setValue(STATE, 0), 3);
        }
    }

    @Override
    public Class<CrossingLightsBlockEntity> getBlockEntityClass() {
        return CrossingLightsBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CrossingLightsBlockEntity> getBlockEntityType() {
        return ETBlockEntities.RAILROAD_LIGHT_CONTROLLER.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        Direction placeDirection = context.getClickedFace().getOpposite();

        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown())) {
            if (placeDirection == Direction.UP || placeDirection == Direction.DOWN) {
                placeDirection = context.getHorizontalDirection();
            }
            state = state.setValue(FACING, placeDirection);
        }

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATE, FACING);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return true;
    }
}
