/// MIT License
///
/// Copyright (c) 2019 simibubi
///
/// Permission is hereby granted, free of charge, to any person obtaining a copy
/// of this software and associated documentation files (the "Software"), to deal
/// in the Software without restriction, including without limitation the rights
/// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
/// copies of the Software, and to permit persons to whom the Software is
/// furnished to do so, subject to the following conditions:
///
/// The above copyright notice and this permission notice shall be included in all
/// copies or substantial portions of the Software.
///
/// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
/// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
/// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
/// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
/// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
/// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
/// SOFTWARE.
/// 
/// Stress config modified to work with Expanding Technologies by curryducker.

package nl.teamdiopside.expandingtechnologies.config;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;

public class ETStress extends ConfigBase {
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();
    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> capacities = new HashMap<>();
    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> impacts = new HashMap<>();

    public ETStress() {}

    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(new String[]{".", ETStress.Comments.su, ETStress.Comments.impact}).push("impact");
        DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
        builder.pop();
        builder.comment(new String[]{".", ETStress.Comments.su, ETStress.Comments.capacity}).push("capacity");
        DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    public @NotNull String getName() {
        return "stressValues";
    }

    public @Nullable DoubleSupplier getImpact(Block block) {
        ResourceLocation id = CatnipServices.REGISTRIES.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = this.impacts.get(id);
        DoubleSupplier var10000;
        if (value == null) {
            var10000 = null;
        } else {
            Objects.requireNonNull(value);
            var10000 = value::get;
        }

        return var10000;
    }

    public @Nullable DoubleSupplier getCapacity(Block block) {
        ResourceLocation id = CatnipServices.REGISTRIES.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = this.capacities.get(id);
        DoubleSupplier var10000;
        if (value == null) {
            var10000 = null;
        } else {
            Objects.requireNonNull(value);
            var10000 = value::get;
        }

        return var10000;
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0.0);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return (builder) -> {
            assertFromET(builder);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExpandingTechnologies.MODID, builder.getName());
            DEFAULT_IMPACTS.put(id, value);
            return builder;
        };
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return (builder) -> {
            assertFromET(builder);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExpandingTechnologies.MODID, builder.getName());
            DEFAULT_CAPACITIES.put(id, value);
            return builder;
        };
    }

    private static void assertFromET(BlockBuilder<?, ?> builder) {
        if (!builder.getOwner().getModid().equals(ExpandingTechnologies.MODID)) {
            throw new IllegalStateException("Non-ET blocks cannot be added to Expanding Technologies's config.");
        }
    }

    private static class Comments {
        static String su = "[in Stress Units]";
        static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";
        static String capacity = "Configure how much stress a source can accommodate for.";
    }
}
