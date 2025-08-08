package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import nl.teamdiopside.expandingtechnologies.behaviour.IBetterContraptionBounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(value = Contraption.class, remap = false)
public class ContraptionMixin implements IBetterContraptionBounds {

    @Unique
    private AABB expandingtechnologies$betterBounds;

    @Override
    public void expandingtechnologies$setBetterBounds(AABB betterBounds) {
        this.expandingtechnologies$betterBounds = betterBounds;
    }

    @Override
    public AABB expandingtechnologies$getBetterBounds() {
        return this.expandingtechnologies$betterBounds;
    }

    @Shadow
    protected Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks;

    @Shadow protected ContraptionWorld world;

    @Override
    public void expandingtechnologies$calculateBetterBounds() {
        expandingtechnologies$setBetterBounds(new AABB(new BlockPos(0, 0, 0)));
        for (BlockPos pos : this.blocks.keySet()) {
            expandingtechnologies$setBetterBounds(expandingtechnologies$getBetterBounds().minmax(new AABB(pos)));
        }
    }
}
