package nl.teamdiopside.expandingtechnologies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.simibubi.create.content.redstone.nixieTube.DoubleFaceAttachedBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

@Mixin(value = {NixieTubeRenderer.class}, remap = false)
public class NixieTubeRendererMixin {
    public NixieTubeRendererMixin() {}

    @Inject(method = {"renderSafe(Lcom/simibubi/create/content/redstone/nixieTube/NixieTubeBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"}, at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/lib/transform/PoseTransformStack;uncenter()Ldev/engine_room/flywheel/lib/transform/Translate;", shift = At.Shift.AFTER))
    private void et$renderSafe(NixieTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        if (Objects.equals(be.getFullText().getString(), "x§") || Objects.equals(be.getFullText().getString(), "§x") || Objects.equals(be.getFullText().getString(), "§§"))
            expandingTechnologies$renderAsLight(be, ms, buffer, light, be.getFullText().getString());
    }

    @Redirect(method = "renderSafe(Lcom/simibubi/create/content/redstone/nixieTube/NixieTubeBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/redstone/nixieTube/NixieTubeBlockEntity;getDisplayedStrings()Lnet/createmod/catnip/data/Couple;"))
    private Couple<String> injected(NixieTubeBlockEntity be) {
        if (Objects.equals(be.getFullText().getString(), "x§") || Objects.equals(be.getFullText().getString(), "§x") || Objects.equals(be.getFullText().getString(), "§§"))
            return Couple.create("", "");
        else return be.getDisplayedStrings();
    }

    @Unique
    private void expandingTechnologies$renderAsLight(NixieTubeBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, String parsedText) {
        BlockState blockState = be.getBlockState();
        Direction facing = expandingtechnologies$facing(blockState);
        assert Minecraft.getInstance().cameraEntity != null;
        TransformStack<PoseTransformStack> msr = TransformStack.of(ms);

        if (facing == Direction.DOWN)
            msr.center()
                    .rotateZ(180)
                    .uncenter();

        CachedBuffers.partial(AllPartialModels.SIGNAL_PANEL, blockState)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        ms.pushPose();
        ms.translate(1 / 2f, 7.5f / 16f, 1 / 2f);

        // Render the left tube
        if (Objects.equals(parsedText, "x§")) {
            ms.pushPose();
            ms.translate(-4 / 16f, 0, 0);
            expandingtechnologies$renderTube(ms, buffer, blockState);
            ms.popPose();
        }

        // Render the right tube
        if (Objects.equals(parsedText, "§x")) {
            ms.pushPose();
            ms.translate(4 / 16f, 0, 0);
            expandingtechnologies$renderTube(ms, buffer, blockState);
            ms.popPose();
        }

        ms.popPose();
    }

    @Unique
    private Direction expandingtechnologies$facing(BlockState pState) {
        return switch (pState.getValue(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> pState.getValue(FACING);
        };
    }

    @Unique
    private void expandingtechnologies$renderTube(PoseStack ms, MultiBufferSource buffer, BlockState blockState) {
        // Render the tube with the desired color and effects
        CachedBuffers.partial(AllPartialModels.SIGNAL_RED, blockState)
                .light(0xF000F0)
                .disableDiffuse()
                .scale(1 + 1 / 16f)
                .renderInto(ms, buffer.getBuffer(RenderTypes.additive()));
    }
}