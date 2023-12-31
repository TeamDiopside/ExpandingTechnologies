package nl.teamdiopside.expandingtechnologies.registry;

import com.google.common.base.Suppliers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.LightDisplaySource;
import nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery.KineticBatteryBlock;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.AssetLookup.partialBaseModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ETBlocks {

    public static final Registrar<Block> REGISTRAR = Suppliers.memoize(() -> RegistrarManager.get(ExpandingTechnologies.MODID)).get().get(Registries.BLOCK);

    public static final RegistrySupplier<Block> KINETIC_BATTERY = REGISTRAR.register(new ResourceLocation(ExpandingTechnologies.MODID, "kinetic_battery"), () -> new KineticBatteryBlock(BlockBehaviour.Properties.of()));

    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

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
            .onRegister(assignDataBehaviour(new LightDisplaySource(), "light_display"))
            .lang("Railroad Light Controller")
            .item()
            .transform(ETUtil.itemModel("block_0"))
            .register();

    public static void register() {}
}
