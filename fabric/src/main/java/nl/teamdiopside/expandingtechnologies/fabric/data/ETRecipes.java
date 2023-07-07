package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

import java.util.function.Consumer;

public class ETRecipes extends FabricRecipeProvider {
    public ETRecipes(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(ETBlocks.RAILROAD_LIGHT_CONTROLLER.get(), 4)
                .define('E', AllItems.ELECTRON_TUBE.get())
                .define('C', AllBlocks.RAILWAY_CASING.get())
                .pattern("ECE")
                .unlockedBy("has_railway_casing", RegistrateRecipeProvider.has(AllBlocks.RAILWAY_CASING.get()))
                .save(exporter, ETUtil.resourceLocation("crafting/railroad_light_controller"));

    }
}
