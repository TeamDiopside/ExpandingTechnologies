package nl.curryducker.expandingtechnologies.blocks.crossinglights;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LightDisplaySource extends SingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof CrossingLightsBlockEntity cbe))
            return EMPTY_LINE;

        MutableComponent text = cbe.getStateForDisplay();
        context.flapDisplayContext = false;

        return text;
    }

    @Override
    public int getPassiveRefreshTicks() {
        return CrossingLightsBlock.lightTimings();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return false;
    }
}
