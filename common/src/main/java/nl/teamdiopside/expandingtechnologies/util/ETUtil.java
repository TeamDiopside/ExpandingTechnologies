package nl.teamdiopside.expandingtechnologies.util;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlock;

import java.util.List;
import java.util.function.Function;

public class ETUtil {

    /**
     * Creative Mode Tab
     */

    public static final CreativeModeTab ExpandingTechnologies = new CreativeModeTab(-1, "expandingTechnologiesTab")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(AllItems.WRENCH.get());
        }
    };

    /**
     * Data Gen
     */

    public static <I extends BlockItem, P> NonNullFunction<ItemBuilder<I, P>, P> itemModel(String suffix) {
        return b -> b.model(itemModelBuilder(suffix)).build();
    }

    public static <I extends BlockItem> NonNullBiConsumer<DataGenContext<Item, I>, RegistrateItemModelProvider> itemModelBuilder(String suffix) {
        return (c, p) -> p.blockItem(() -> c.getEntry().getBlock(), "/" + suffix);
    }

    public static <T extends Block> void horizontalUvBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                        .uvLock(true)
                        .build());
    }

    /**
     * Ponder
     */

    public static void flash1(SceneBuilder scene, SceneBuildingUtil util, BlockPos nixiePos, BlockPos linkPos, BlockPos lightController) {
        scene.world.modifyBlock(lightController, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 1), false);
        scene.world.modifyBlockEntityNBT(util.select.position(nixiePos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"x§\"}"));
        scene.world.flashDisplayLink(linkPos);
    }

    public static void flash1(SceneBuilder scene, SceneBuildingUtil util, List<BlockPos> nixieList, List<BlockPos> linkList, BlockPos lightController) {
        scene.world.modifyBlock(lightController, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 1), false);
        for (BlockPos pos : nixieList)
            scene.world.modifyBlockEntityNBT(util.select.position(pos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"x§\"}"));
        for (BlockPos pos : linkList)
            scene.world.flashDisplayLink(pos);
    }

    public static void flash2(SceneBuilder scene, SceneBuildingUtil util, BlockPos nixiePos, BlockPos linkPos, BlockPos lightController) {
        scene.world.modifyBlock(lightController, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 2), false);
        scene.world.modifyBlockEntityNBT(util.select.position(nixiePos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"§x\"}"));
        scene.world.flashDisplayLink(linkPos);
    }

    public static void flash2(SceneBuilder scene, SceneBuildingUtil util, List<BlockPos> nixieList, List<BlockPos> linkList, BlockPos lightController) {
        scene.world.modifyBlock(lightController, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 2), false);
        for (BlockPos pos : nixieList)
            scene.world.modifyBlockEntityNBT(util.select.position(pos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"§x\"}"));
        for (BlockPos pos : linkList)
            scene.world.flashDisplayLink(pos);
    }

    public static void nixieOff(SceneBuilder scene, SceneBuildingUtil util, BlockPos nixie_pos, BlockPos link_pos, BlockPos light_block) {
        scene.world.modifyBlock(light_block, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 0), false);
        scene.world.modifyBlockEntityNBT(util.select.position(nixie_pos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"§§\"}"));
        scene.world.flashDisplayLink(link_pos);
    }

    public static void nixieOff(SceneBuilder scene, SceneBuildingUtil util, List<BlockPos> nixie_list, List<BlockPos> link_list, BlockPos light_block) {
        scene.world.modifyBlock(light_block, blockState -> blockState.setValue(CrossingLightsBlock.STATE, 0), false);
        for (BlockPos pos : nixie_list)
            scene.world.modifyBlockEntityNBT(util.select.position(pos), NixieTubeBlockEntity.class, nbt -> nbt.putString("CustomText", "{\"text\":\"§§\"}"));
        for (BlockPos pos : link_list)
            scene.world.flashDisplayLink(pos);
    }
}
