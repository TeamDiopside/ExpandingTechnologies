package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = {NixieTubeBlock.class})
public class NixieTubeMixin {
    public NixieTubeMixin() {}

    @Inject(method = {"use"}, at = {@At("HEAD")}, cancellable = true)
    private void et$use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir) {
        String help = ((NixieTubeBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).getFullText().getString();
        if (help.equals("x§") || help.equals("§x") || help.equals("§§")) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}