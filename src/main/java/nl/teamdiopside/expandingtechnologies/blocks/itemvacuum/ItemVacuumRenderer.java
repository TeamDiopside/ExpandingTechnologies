package nl.teamdiopside.expandingtechnologies.blocks.itemvacuum;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import nl.teamdiopside.expandingtechnologies.registry.ETPartialModels;

public class ItemVacuumRenderer extends KineticBlockEntityRenderer<ItemVacuumBlockEntity> {
    public ItemVacuumRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(ItemVacuumBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
        Level level = be.getLevel();
        if (level != null) {
            SuperByteBuffer shaft = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), Direction.UP);
            standardKineticRotationTransform(shaft, be, light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
            SuperByteBuffer fan = CachedBuffers.partialFacing(ETPartialModels.VACUUM_FAN, be.getBlockState(), Direction.UP);
            standardKineticRotationTransform(fan, be, light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }
    }
}
