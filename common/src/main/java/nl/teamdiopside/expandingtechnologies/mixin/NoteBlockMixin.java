package nl.teamdiopside.expandingtechnologies.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {NoteBlock.class})
public class NoteBlockMixin {
    public NoteBlockMixin() {}

    @Inject(method = {"playNote"}, at = {@At("HEAD")}, cancellable = true)
    private void et$playNote(Entity entity, BlockState blockState, Level level, BlockPos blockPos, CallbackInfo ci) {
        if (level.getBlockState(blockPos.relative(Direction.DOWN)).getBlock() instanceof CrossingLightsBlock) {
            ci.cancel();
        }
    }
}