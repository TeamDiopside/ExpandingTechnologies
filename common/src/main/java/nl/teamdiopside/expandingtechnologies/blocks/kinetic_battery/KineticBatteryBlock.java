package nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import org.jetbrains.annotations.NotNull;

public class KineticBatteryBlock extends DirectionalKineticBlock implements IBE<KineticBatteryBlockEntity> {

    public static final BooleanProperty CHARGING = BooleanProperty.create("charging");

    public KineticBatteryBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(CHARGING, true).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return AllShapes.MOTOR_BLOCK.get(blockState.getValue(FACING));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CHARGING);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CHARGING, !blockState.getValue(CHARGING)));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public Class<KineticBatteryBlockEntity> getBlockEntityClass() {
        return KineticBatteryBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBatteryBlockEntity> getBlockEntityType() {
        return ETBlockEntities.KINETIC_BATTERY.get();
    }
}
