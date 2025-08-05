package nl.teamdiopside.expandingtechnologies.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.ParrotElement;
import net.createmod.ponder.api.element.ParrotPose;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SmartTrainObserverPonder {

    public static void constructing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("smart_train_observer_instruction", "Using the Smart Train Observer");
        int xOffset = 5;
        scene.configureBasePlate(xOffset, 0, 9);
        scene.scaleSceneView(0.65F);
        scene.showBasePlate();
        scene.idle(5);

        Selection mainTrack = util.select().fromTo(xOffset, 1, 6, xOffset + 8, 1, 6);
        Selection trainTrack = util.select().fromTo(xOffset + 9, 1, 6, xOffset + 13, 1, 6);
        Selection endTrack = util.select().fromTo(xOffset - 1, 1, 6, 0, 1, 6);
        BlockPos observer = util.grid().at(xOffset + 4, 1, 3);

        scene.world().showSection(mainTrack, Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(observer), Direction.DOWN);

        scene.idle(20);
        scene.overlay().showText(80)
                .text("The Smart Train Observer is an advanced variant of the Train Observer")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(observer, Direction.WEST))
                .placeNearTarget();
        scene.idle(90);

        scene.overlay().showText(90)
                .text("It is able to detect trains based on a filter, which can be set by clicking the block.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(observer, Direction.WEST))
                .placeNearTarget();
        scene.idle(30);
        scene.overlay().showControls(util.vector().of(xOffset + 4.5, 1.5, 3), Pointing.RIGHT, 60).rightClick();
        scene.idle(70);

        scene.overlay().showText(70)
                .text("You can, for example, filter trains based on their name!")
                .attachKeyFrame();
        scene.idle(20);

        // Show Train
        scene.world().showSection(trainTrack, Direction.UP);
        ElementLink<WorldSectionElement> train = scene.world().showIndependentSection(util.select().fromTo(xOffset + 9, 2, 5, xOffset + 13, 3, 7), Direction.UP);

        ElementLink<ParrotElement> birb = scene.special().createBirb(util.vector().centerOf(xOffset + 12, 3, 6), ParrotPose.FacePointOfInterestPose::new);
        scene.special().conductorBirb(birb, true);
        scene.special().movePointOfInterest(util.grid().at(0, 4, 6));

        BlockPos trainControls = util.grid().at(xOffset + 11, 3, 6);
        BlockPos bogey = util.grid().at(xOffset + 11, 2, 6);
        scene.overlay().showText(50)
                .text("\"Parrot Express\"")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector().blockSurface(trainControls, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showText(150)
                .text("\"* Express\"")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().blockSurface(observer, Direction.WEST))
                .placeNearTarget();
        scene.idle(60);
        scene.overlay().showText(80)
                .text("'*' can be used as a wildcard for your filter.");

        int trainRideDistance = 14;
        int trainRideSpeed = 10; // Ticks per block
        int trainRideDuration = trainRideSpeed * trainRideDistance;
        scene.world().moveSection(train, util.vector().of(-trainRideDistance, 0, 0), trainRideDuration);
        scene.special().moveParrot(birb, util.vector().of(-trainRideDistance, 0, 0), trainRideDuration);
        scene.world().animateBogey(bogey, trainRideDistance, trainRideDuration);

        int afterBlocks = 6;
        int trainLength = 3;
        scene.idle(afterBlocks * trainRideSpeed);
        scene.world().hideSection(trainTrack, Direction.DOWN);
        scene.world().toggleRedstonePower(util.select().position(observer));
        scene.idle(trainLength * trainRideSpeed);

        scene.overlay().showText(70)
                .text("Other conditions include the detection of destinations, conductors and passengers.");

        scene.world().toggleRedstonePower(util.select().position(observer));
        scene.world().showSection(endTrack, Direction.UP);
        scene.idle((trainRideDistance - afterBlocks - trainLength + 3) * trainRideSpeed);
        scene.world().hideSection(endTrack, Direction.DOWN);
        scene.world().hideIndependentSection(train, Direction.DOWN);
        scene.special().hideElement(birb, Direction.DOWN);

        scene.markAsFinished();
    }
}
