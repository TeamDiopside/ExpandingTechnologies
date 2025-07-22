package nl.teamdiopside.expandingtechnologies.blocks.itemvacuum;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemVacuumBlock extends KineticBlock implements IBE<ItemVacuumBlockEntity> {

    public ItemVacuumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<ItemVacuumBlockEntity> getBlockEntityClass() {
        return ItemVacuumBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ItemVacuumBlockEntity> getBlockEntityType() {
        return ETBlockEntities.ITEM_VACUUM.get();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return getBlockEntityType().create(blockPos, blockState);
    }
}
