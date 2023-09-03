package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ETRecipes extends FabricRecipeProvider {
    public ETRecipes(FabricDataOutput fabricDataOutput) {
        super(fabricDataOutput);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ETBlocks.RAILROAD_LIGHT_CONTROLLER.get(), 4)
                .define('E', AllItems.ELECTRON_TUBE.get())
                .define('C', AllBlocks.RAILWAY_CASING.get())
                .pattern("ECE")
                .unlockedBy("has_railway_casing", RegistrateRecipeProvider.has(AllBlocks.RAILWAY_CASING.get()))
                .save(exporter, ETUtil.resourceLocation("crafting/railroad_light_controller"));
    }

    private CreateRecipeProvider.GeneratedRecipe sequencedAssembly(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        return c -> transform.apply(new SequencedAssemblyRecipeBuilder(ETUtil.resourceLocation(name))).build(c);
    }

    private CreateRecipeProvider.GeneratedRecipe itemApplication(String name, UnaryOperator<ProcessingRecipeBuilder<ItemApplicationRecipe>> transform) {
        return createRecipe(name, transform, AllRecipeTypes.ITEM_APPLICATION.getSerializer());
    }

    private <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe createRecipe(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform, ProcessingRecipeSerializer<T> serializer) {
        return c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), ETUtil.resourceLocation(name))).build(c);
    }
}
