package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.logistics.packagerLink.PackagerLinkBlockEntity;
import com.simibubi.create.content.redstone.displayLink.LinkWithBulbBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.behaviour.ITickTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour.keepAlive;

@Mixin(value = PackagerLinkBlockEntity.class)
public abstract class PackagerLinkBlockEntityMixin extends LinkWithBulbBlockEntity implements ITickTracking {

    @Unique
    private long expandingtechnologies$lastTick;

    public PackagerLinkBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public long expandingtechnologies$getLastTick() {
        return expandingtechnologies$lastTick;
    }

    @Override
    public void expandingtechnologies$setLastTick(long expandingtechnologies$lastTick) {
        this.expandingtechnologies$lastTick = expandingtechnologies$lastTick;
    }

    @Override
    public boolean expandingtechnologies$isTicking() {
        Level level = getLevel();
        return level != null && level.getGameTime() - expandingtechnologies$lastTick < 10;
    }

    @Override
    public void tick() {
        Level level = this.getLevel();
        if (level == null) {
            super.tick();
            return;
        }
        PackagerLinkBlockEntity thisObject = (PackagerLinkBlockEntity)(Object)this;
        // Block has been out of ticking range, without chunk unloading.
        // If lastTick == 0, we can assume the block entity has just been loaded.
        // When the block entity loads we have the same behaviour in initialize.
        if (expandingtechnologies$getLastTick() != 0 && !expandingtechnologies$isTicking()) {
            // Revive the behaviour
            keepAlive(thisObject.behaviour);
        }
        expandingtechnologies$setLastTick(level.getGameTime());
        // Super ticks behaviours and updates lastTick.
        // isTicking should be true again after this.
        super.tick();
    }
}
