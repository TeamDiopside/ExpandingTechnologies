package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.simibubi.create.AllBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

//public class ETItemTags extends FabricTagProvider<Item>  {
//    public ETItemTags(FabricDataGenerator dataGenerator) {
//        super(dataGenerator, Registry.ITEM);
//    }
//
//    public static final TagKey<Item> SMALL_COGWHEELS = add("small_cogwheels");
//
//    static TagKey<Item> add(String name) {
//        return TagKey.create(Registry.ITEM_REGISTRY, ETUtil.resourceLocation(name));
//    }
//
//    @Override
//    protected void generateTags() {
//        getOrCreateTagBuilder(SMALL_COGWHEELS)
//                .add(AllBlocks.COGWHEEL.get().asItem())
//                .addOptionalTag(new ResourceLocation("extendedgears", "small_cogwheel"))
//        ;
//    }
//}
