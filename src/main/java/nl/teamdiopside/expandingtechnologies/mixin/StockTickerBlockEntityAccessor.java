package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = StockTickerBlockEntity.class, remap = false)
public interface StockTickerBlockEntityAccessor {

    @Accessor("previouslyUsedAddress")
    void setPreviouslyUsedAddress(String value);
}
