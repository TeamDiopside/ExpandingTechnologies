package nl.teamdiopside.expandingtechnologies.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ItemVacuumPonder {

    public static void itemVacuumConstructing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("item_vacuum_instruction", "Using the Item Vacuum");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(.9f);
        scene.showBasePlate();
        scene.idle(5);

        BlockPos itemVacuum = util.grid().at(2, 3,  2);
        Selection kineticsPart1 = util.select().fromTo(3, 1, 3, 4, 5, 3);
        Selection kineticsPart2 = util.select().fromTo(2, 4, 2, 2, 5, 2);
        Selection kineticsPart3 = util.select().fromTo(4, 1, 0, 7, 2, 2);
        Selection kinetics = kineticsPart1.add(kineticsPart3);
        Selection kineticsDoubleSpeed = kineticsPart2.add(util.select().position(itemVacuum));
        Selection storage = util.select().fromTo(2, 1, 2, 2, 2, 2);

        // Show everything except station
        scene.world().showSection(kineticsPart1, Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(kineticsPart2, Direction.EAST);
        scene.idle(10);
        scene.world().showSection(kineticsPart3, Direction.SOUTH);
        scene.idle(10);
        scene.world().showSection(util.select().position(itemVacuum), Direction.UP);
        scene.idle(10);
        int speed = 64;
        scene.world().setKineticSpeed(kinetics, speed);
        scene.world().setKineticSpeed(kineticsDoubleSpeed, speed * -2);
        scene.idle(40);

        // Explain
        ItemStack stack = new ItemStack(Items.COBBLESTONE, 64);
        scene.world().createItemOnBelt(util.grid().at(7, 1, 1), Direction.DOWN, stack);
        scene.idle(20);
        scene.overlay().showText(90)
                .text("The Item Vacuum is able to pick up items around it.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(itemVacuum, Direction.WEST))
                .placeNearTarget();
        scene.idle(40);
        scene.world().modifyEntities(ItemEntity.class, itemEntity -> itemEntity.setItem(new ItemStack(Items.COBBLESTONE, 48)));
        scene.idle(10);
        scene.world().modifyEntities(ItemEntity.class, itemEntity -> itemEntity.setItem(new ItemStack(Items.COBBLESTONE, 32)));
        scene.idle(10);
        scene.world().modifyEntities(ItemEntity.class, itemEntity -> itemEntity.setItem(new ItemStack(Items.COBBLESTONE, 16)));
        scene.idle(10);
        scene.world().modifyEntities(ItemEntity.class, Entity::discard);
        scene.idle(40);

        // Explain storage
        scene.world().showSection(storage, Direction.EAST);
        scene.idle(10);

        scene.overlay().showText(90)
                .text("The items can be extracted from underneath.")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2,  2), Direction.WEST))
                .placeNearTarget();
        scene.idle(20);
        scene.overlay().showControls(util.vector().of(2, 2, 2), Pointing.LEFT, 60).withItem(new ItemStack(Blocks.COBBLESTONE, 64));
        scene.idle(90);

        // Show
        Vec3 filterLocation = new Vec3(2.5, 3, 2);
        scene.overlay().showText(90)
                .text("A filter can be used to determine which item will be picked up.")
                .attachKeyFrame()
                .pointAt(filterLocation)
                .placeNearTarget();

        scene.idle(20);
        scene.overlay().showFilterSlotInput(filterLocation, Direction.SOUTH, 60);
        scene.idle(80);

        scene.markAsFinished();
    }
}
