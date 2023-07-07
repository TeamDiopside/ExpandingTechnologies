package nl.curryducker.expandingtechnologies.util;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.redstone.diodes.PoweredLatchBlock;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.ParrotElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.List;

public class PonderScenes {

    /**
     * Railroad Lights
     */

    public static void constructing(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("railroad_lights_instruction", "Using the Railroad Light Controller");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(.9f);
        scene.showBasePlate();
        scene.idle(5);

        Selection lever = util.select.fromTo(3, 1, 0, 3, 1, 1);
        BlockPos railroadLights = util.grid.at(3, 1,  2);
        BlockPos displayLink = util.grid.at(2, 1,  2);
        BlockPos nixie = util.grid.at(1, 1,  3);

        scene.world.showSection(util.select.position(railroadLights), Direction.DOWN);

        scene.overlay.showText(80)
                .text("The Railroad Light Controller can be used to make railroad crossings a little more beautiful.")
                .attachKeyFrame()
                .pointAt(util.vector.blockSurface(railroadLights, Direction.WEST))
                .placeNearTarget();
        scene.idle(90);

        scene.world.showSection(util.select.position(nixie), Direction.DOWN);
        scene.idle(10);
        scene.overlay.showControls(new InputWindowElement(util.vector.of(1.5, 2, 3.5), Pointing.DOWN).withItem(AllBlocks.DISPLAY_LINK.asStack()).rightClick(), 60);
        scene.idle(10);
        scene.overlay.showText(90)
                .text("To use it, attach a Display Link targeted at Nixie Tubes.")
                .attachKeyFrame()
                .pointAt(util.vector.blockSurface(nixie, Direction.WEST))
                .placeNearTarget();
        scene.idle(10);
        scene.overlay.chaseBoundingBoxOutline(PonderPalette.OUTPUT, util.select.position(displayLink), new AABB(nixie).deflate(0, 2 / 16f, 5 / 16f).move(0, -2 / 16f, 0), 60);
        scene.overlay.chaseBoundingBoxOutline(PonderPalette.INPUT, util.select.position(displayLink), new AABB(railroadLights), 60);
        scene.idle(10);
        scene.world.showSection(util.select.position(displayLink), Direction.EAST);
        scene.idle(80);

        scene.world.showSection(lever, Direction.SOUTH);

        scene.overlay.showText(75)
                .text("Then, just power the block with redstone to turn the lights on.")
                .attachKeyFrame()
                .pointAt(util.vector.blockSurface(util.grid.at(3, 1,  0), Direction.WEST))
                .placeNearTarget();
        scene.idle(85);

        scene.world.toggleRedstonePower(lever.add(util.select.position(railroadLights)));
        scene.effects.indicateRedstone(util.grid.at(3, 1, 0));
        for (int i = 0; i < 5; i++) {
            ETUtil.flash1(scene, util, nixie, displayLink, railroadLights);
            scene.idle(10);
            ETUtil.flash2(scene, util, nixie, displayLink, railroadLights);
            scene.idle(10);
        }

        scene.world.toggleRedstonePower(lever.add(util.select.position(railroadLights)));
        scene.effects.indicateRedstone(util.grid.at(3, 1, 0));
        ETUtil.nixieOff(scene, util, nixie, displayLink, railroadLights);

        scene.markAsFinished();
    }

    public static void practicalExample(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("railroad_lights_example", "Railroad Crossing Lights Example");
        // BasePlate size and offset from NW corner schematic
        scene.configureBasePlate(5, 0, 12);
        // Cam scale, zijn voorbeelden van in andere scenes op basis van size
        scene.scaleSceneView(.65f);
        scene.showBasePlate();
        scene.idle(5);

        // Place tracks from left to right
        for (int i = 16; i >= 5; i--) {
            scene.world.showSection(util.select.position(i, 1, 8), Direction.DOWN);
            scene.idle(2);
        }

        // Toggle op de Train Controls
        scene.world.toggleControls(util.grid.at(18, 3, 8));
        scene.idle(8);

        // Walls
        scene.world.showSection(util.select.fromTo(7, 1, 6, 9, 1, 6), Direction.DOWN);
        scene.world.showSection(util.select.fromTo(12, 1, 6, 14, 1, 6), Direction.DOWN);
        scene.world.showSection(util.select.fromTo(7, 1, 10, 9, 1, 10), Direction.DOWN);
        scene.world.showSection(util.select.fromTo(12, 1, 10, 14, 1, 10), Direction.DOWN);
        scene.idle(5);

        // Nixie Tubes
        scene.world.showSection(util.select.position(9, 2, 6), Direction.DOWN);
        scene.world.showSection(util.select.position(12, 2, 6), Direction.DOWN);
        scene.world.showSection(util.select.position(9, 2, 10), Direction.DOWN);
        scene.world.showSection(util.select.position(12, 2, 10), Direction.DOWN);
        scene.idle(10);

        // Train Observers
        scene.world.showSection(util.select.position(6, 1, 4), Direction.DOWN);
        scene.world.showSection(util.select.position(15, 1, 4), Direction.DOWN);
        scene.idle(5);

        // Redstone Links on Observers
        scene.world.showSection(util.select.position(6, 2, 4), Direction.DOWN);
        scene.world.showSection(util.select.position(15, 2, 4), Direction.DOWN);
        scene.idle(10);

        // Light Controller, Toggle Latch and Links
        Selection lightControllerArea = util.select.fromTo(10, 1, 2, 12, 1, 2);
        scene.world.showSection(lightControllerArea, Direction.DOWN);
        scene.idle(5);

        // Display links
        scene.world.showSection(util.select.position(12, 1, 1), Direction.SOUTH);
        scene.world.showSection(util.select.position(12, 1, 3), Direction.NORTH);
        scene.world.showSection(util.select.position(13, 1, 2), Direction.WEST);
        scene.world.showSection(util.select.position(12, 2, 2), Direction.DOWN);
        scene.idle(5);

        // Text
        scene.overlay.showText(90)
                .text("You can use Train Observers to detect upcoming trains.")
                .attachKeyFrame()
                .pointAt(util.vector.blockSurface(util.grid.at(6, 1, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);

        scene.addKeyframe();

        scene.overlay.showText(90)
                .text("Connect them to a Powered Toggle Latch using Redstone Links to power the block.")
                .pointAt(util.vector.blockSurface(util.grid.at(10, 1, 2), Direction.SOUTH))
                .placeNearTarget();
        scene.idle(100);

        scene.addKeyframe();

        // Track over the edge
        scene.world.showSection(util.select.fromTo(17, 1, 8, 21, 1, 8), Direction.UP);

        // Trein
        ElementLink<WorldSectionElement> train = scene.world.showIndependentSection(util.select.fromTo(18, 2, 7, 21, 3, 9), Direction.UP);

        // Birb
        ElementLink<ParrotElement> birb = scene.special.createBirb(util.vector.centerOf(19, 3, 8), ParrotElement.FacePointOfInterestPose::new);
        scene.special.conductorBirb(birb, true);
        // POI naar andere kant van spoor
        scene.special.movePointOfInterest(util.grid.at(0, 4, 8));
        scene.idle(20);

        Selection leftObserver = util.select.fromTo(15, 1, 4, 15, 2, 4);
        Selection rightObserver = util.select.fromTo(6, 1, 4, 6, 2, 4);
        Selection signal = util.select.position(10, 1, 2);
        BlockPos latchPos = util.grid.at(11, 1, 2);
        Selection latch = util.select.position(latchPos);
        BlockPos lightControllerPos = util.grid.at(12, 1, 2);

        int trainRideDuration = 170;
        int trainRideDistance = 17;
        int trainRideSpeed = trainRideDuration / trainRideDistance;
        // trein bewegen
        scene.world.moveSection(train, util.vector.of(-trainRideDistance, 0, 0), trainRideDuration);
        // tegelijk parrot bewegen
        scene.special.moveParrot(birb, util.vector.of(-trainRideDistance, 0, 0), trainRideDuration);
        // bogey animaten
        scene.world.animateBogey(util.grid.at(19, 2, 8), trainRideDistance, trainRideDuration);
        // 140/28=5, 5 ticks per block, observer zit 5 blokken van startpunt
        scene.idle(3 * trainRideSpeed);
        // Observer 1
        // Observer moet aan dus toggle power en ook bij selection rond block
        scene.world.toggleRedstonePower(leftObserver.add(lightControllerArea));
        // Powered toggle latch heeft een extra state (kijk maar in bestaande ponder naar het verschil in uiterlijk)
        scene.world.modifyBlock(latchPos, blockState -> blockState.setValue(PoweredLatchBlock.POWERING, true), false);

        // Lijsten voor het flashen
        List<BlockPos> nixie_pos = List.of(util.grid.at(9, 2, 6), util.grid.at(9, 2, 10), util.grid.at(12, 2, 6), util.grid.at(12, 2, 10));
        List<BlockPos> link_pos = List.of(util.grid.at(12, 1, 1), util.grid.at(12, 1, 3), util.grid.at(13, 1, 2), util.grid.at(12, 2, 2));

        // Tussen elke flash zit 10 ticks, maar er gebeurt nog van alles tijdens het flashen dus goed rekenen, laatste argument is de locatie van het block
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        // Na 10 ticks (dus 1 block) is de trein van de observer dus mag de power weer uit, maar de andere state van de latch blijft dus aan
        scene.world.toggleRedstonePower(leftObserver.add(latch).add(signal));
        ETUtil.flash2(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash2(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(5);

        // Spoor links terug naar beneden, spoor rechts komt boven
        scene.world.hideSection(util.select.fromTo(17, 1, 8, 21, 1, 8), Direction.DOWN);
        scene.world.showSection(util.select.fromTo(0, 1, 8, 4, 1, 8), Direction.UP);
        scene.idle(5);
        ETUtil.flash2(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash2(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash2(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        ETUtil.flash1(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(10);
        // Observer 2
        // Andere observer moet aan dus toggle power op andere observer en bij de selection van het blok
        scene.world.toggleRedstonePower(rightObserver.add(lightControllerArea));
        // Nu moet de latch weer uit
        scene.world.modifyBlock(latchPos, blockState -> blockState.setValue(PoweredLatchBlock.POWERING, false), false);
        // Nixies uit
        ETUtil.nixieOff(scene, util, nixie_pos, link_pos, lightControllerPos);
        scene.idle(trainRideSpeed);
        // Trein is weer doorgereden dus untoggle
        scene.world.toggleRedstonePower(rightObserver.add(latch).add(signal));

        // even wachten tot trein aan het eind is en stil staat
        scene.idle(25);
        // Verstop trein, birb en spoor samen
        scene.world.hideIndependentSection(train, Direction.DOWN);
        scene.special.hideElement(birb, Direction.DOWN);
        scene.world.hideSection(util.select.fromTo(0, 1, 8, 4, 1, 8), Direction.DOWN);

        //tada
        scene.markAsFinished();
    }
}
