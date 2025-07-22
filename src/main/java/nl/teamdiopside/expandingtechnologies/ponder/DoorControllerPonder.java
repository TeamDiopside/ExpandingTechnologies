package nl.teamdiopside.expandingtechnologies.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DoorControllerPonder {

    public static void doorControlConstructing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("door_controller_instruction", "Using the Door Controller");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(.9f);
        scene.showBasePlate();
        scene.idle(5);

        BlockPos doorController = util.grid().at(2, 1,  2);
        BlockPos station = util.grid().at(2, 2,  2);
        Selection lever = util.select().fromTo(2, 1, 0, 2, 1, 1);
        Selection lever2 = util.select().fromTo(1, 1,  2, 0, 1,  2);
        BlockPos lever3 = util.grid().at(2, 1,  3);
        BlockPos lever4 = util.grid().at(3, 1,  2);

        // Show everything except station
        scene.world().showSection(util.select().position(doorController), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(lever, Direction.DOWN);
        scene.world().showSection(lever2, Direction.DOWN);
        scene.world().showSection(util.select().position(lever3), Direction.NORTH);
        scene.world().showSection(util.select().position(lever4), Direction.WEST);

        scene.idle(40);
        // Show station and explanation
        scene.world().showSection(util.select().position(station), Direction.DOWN);
        scene.overlay().showText(80)
                .text("The Door Controller can be used to specify which train doors to open at a station using redstone.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(doorController, Direction.WEST))
                .placeNearTarget();
        scene.idle(90);

        // Explain powering
        scene.overlay().showText(80)
                .text("When powering a specific side, that side of the train will have it's doors opened on arrival.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1,  0), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);
        scene.world().toggleRedstonePower(lever);
        scene.world().modifyBlock(doorController, state -> state.setValue(BlockStateProperties.NORTH, true), false);
        scene.effects().indicateRedstone(util.grid().at(2, 1, 0));
        scene.idle(50);

        // Use second lever
        scene.overlay().showText(80)
                .text("Multiple sides of the block can be powered to open doors on multiple sides of the train.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(util.grid().at(0, 1,  2), Direction.WEST))
                .placeNearTarget();

        scene.idle(40);
        scene.world().toggleRedstonePower(lever2);
        scene.effects().indicateRedstone(util.grid().at(0, 1, 2));
        scene.world().modifyBlock(doorController, state -> state.setValue(BlockStateProperties.WEST, true), false);
        scene.idle(50);

        scene.markAsFinished();
    }
}
