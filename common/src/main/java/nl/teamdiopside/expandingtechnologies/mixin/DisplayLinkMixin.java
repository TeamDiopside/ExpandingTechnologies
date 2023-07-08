package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {DisplayLinkBlock.class})
public class DisplayLinkMixin {
    public DisplayLinkMixin() {}

    @Inject(method = {"shouldBePowered"}, at = {@At("RETURN")}, cancellable = true)
    private void et$shouldBePowered(BlockState state, Level worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (worldIn.getBlockState(pos.relative(state.getValue(DisplayLinkBlock.FACING).getOpposite())).getBlock() instanceof CrossingLightsBlock && worldIn.getBlockState(pos.relative(state.getValue(DisplayLinkBlock.FACING).getOpposite())).getValue(CrossingLightsBlock.STATE) == 0) {
            cir.setReturnValue(true);
        }
    }
}