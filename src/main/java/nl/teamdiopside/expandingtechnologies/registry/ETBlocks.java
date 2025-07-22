package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import nl.teamdiopside.expandingtechnologies.config.ETStress;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.LightDisplaySource;
import nl.teamdiopside.expandingtechnologies.blocks.doorcontroller.DoorControllerBlock;
import nl.teamdiopside.expandingtechnologies.blocks.itemvacuum.ItemVacuumBlock;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ETBlocks {
    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

    public static final RegistryEntry<LightDisplaySource> LIGHT_DISPLAY = REGISTRATE.displaySource("light_display", LightDisplaySource::new).register();

    public static final BlockEntry<CrossingLightsBlock> RAILROAD_LIGHT_CONTROLLER = REGISTRATE.block("railroad_light_controller", CrossingLightsBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> ETUtil.horizontalUvBlock(c, p, state -> switch (state.getValue(CrossingLightsBlock.STATE)) {
                case 1 -> partialBaseModel(c, p, "1");
                case 2 -> partialBaseModel(c, p, "2");
                default -> partialBaseModel(c, p, "0");
            }))
            .transform(displaySource(LIGHT_DISPLAY))
            .lang("Railroad Light Controller")
            .item()
            .transform(ETUtil.itemModel("block_0"))
            .register();

    public static final BlockEntry<DoorControllerBlock> DOOR_CONTROLLER = REGISTRATE.block("door_controller", DoorControllerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(p -> p.isRedstoneConductor((state, blockGetter, pos) -> false))
            .transform(pickaxeOnly())
            .lang("Door Controller")
            .item()
            .transform(ETUtil.itemModel("block_0"))
            .register();

    public static final BlockEntry<ItemVacuumBlock> ITEM_VACUUM = REGISTRATE.block("item_vacuum", ItemVacuumBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(p -> p.isRedstoneConductor((state, blockGetter, pos) -> false))
            .transform(pickaxeOnly())
            .transform(ETStress.setImpact(4.0))
            .lang("Item Vacuum")
            .item()
            .transform(ETUtil.itemModel("block_0"))
            .register();

    public static void register() {}
}
