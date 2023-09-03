package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.data.LangPartial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = LangMerger.class, remap = false)
public interface AccessorLangMerger {
    // Why Create, why
    @Mutable
    @Accessor
    void setLangPartials(LangPartial[] langPartials);
}
