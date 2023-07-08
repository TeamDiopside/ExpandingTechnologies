package nl.teamdiopside.expandingtechnologies.mixin;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.simibubi.create.content.redstone.nixieTube.DoubleFaceAttachedBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

@Mixin(value = {NixieTubeRenderer.class})
public class NixieTubeRendererMixin {
    public NixieTubeRendererMixin() {}

    @Inject(method = {"renderSafe(Lcom/simibubi/create/content/redstone/nixieTube/NixieTubeBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"}, at = @At(value = "INVOKE", target = "Lcom/jozufozu/flywheel/util/transform/TransformStack;unCentre()Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void et$renderSafe(NixieTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (Objects.equals(be.getFullText().getString(), "x§") || Objects.equals(be.getFullText().getString(), "§x") || Objects.equals(be.getFullText().getString(), "§§")) {
            renderAsLight(be, ms, buffer, light, be.getFullText().getString());
        }
    }

    private void renderAsLight(NixieTubeBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, String parsedText) {
        BlockState blockState = be.getBlockState();
        Direction facing = facing(blockState);
        assert Minecraft.getInstance().cameraEntity != null;
        TransformStack msr = TransformStack.cast(ms);

        if (facing == Direction.DOWN)
            msr.centre()
                    .rotateZ(180)
                    .unCentre();

        CachedBufferer.partial(AllPartialModels.SIGNAL_PANEL, blockState)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        ms.pushPose();
        ms.translate(1 / 2f, 7.5f / 16f, 1 / 2f);

        // Render the left tube
        if (Objects.equals(parsedText, "x§")) {
            ms.pushPose();
            ms.translate(-4 / 16f, 0, 0);
            renderTube(ms, buffer, blockState);
            ms.popPose();
        }

        // Render the right tube
        if (Objects.equals(parsedText, "§x")) {
            ms.pushPose();
            ms.translate(4 / 16f, 0, 0);
            renderTube(ms, buffer, blockState);
            ms.popPose();
        }

        ms.popPose();
    }

    @Unique
    private Direction facing(BlockState pState) {
        return switch (pState.getValue(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> pState.getValue(FACING);
        };
    }

    private void renderTube(PoseStack ms, MultiBufferSource buffer, BlockState blockState) {
        // Render the tube with the desired color and effects
        CachedBufferer.partial(AllPartialModels.SIGNAL_RED, blockState)
                .light(0xF000F0)
                .disableDiffuse()
                .scale(1 + 1 / 16f)
                .renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
    }
}