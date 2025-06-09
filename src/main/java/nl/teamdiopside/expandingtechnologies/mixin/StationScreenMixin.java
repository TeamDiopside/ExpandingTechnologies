package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.decoration.slidingDoor.DoorControl;
import com.simibubi.create.content.trains.station.AbstractStationScreen;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.StationScreen;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import net.createmod.catnip.data.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.blocks.doorcontroller.DoorControllerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(value = StationScreen.class, remap = false)
public abstract class StationScreenMixin extends AbstractStationScreen {

    public StationScreenMixin(StationBlockEntity be, GlobalStation station) {
        super(be, station);
    }

    @Redirect(method = {"m_7856_"}, at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/slidingDoor/DoorControl;createWidget(IILjava/util/function/Consumer;Lcom/simibubi/create/content/decoration/slidingDoor/DoorControl;)Lnet/createmod/catnip/data/Pair;"))
    private Pair<ScrollInput, Label> injected(int x, int y, Consumer<DoorControl> callback, DoorControl initial) {
        if (blockEntity != null) {
            Level level = blockEntity.getLevel();
            BlockPos doorControlPos = blockEntity.getBlockPos().below();
            if (level != null) {
                BlockState doorControlState = level.getBlockState(doorControlPos);
                if (doorControlState.getBlock() instanceof DoorControllerBlock) {
                    Integer color = ChatFormatting.DARK_RED.getColor();
                    assert color != null;
                    Label label = (new Label(x + 4, y + 6, Component.empty())).withShadow().colored(color);
                    label.text = Component.literal("---");
                    ScrollInput input = new SelectionScrollInput(x, y, 53, 16);
                    return Pair.of(input, label);
                }
            }
        }
        return DoorControl.createWidget(x, y, callback, initial);
    }
}
