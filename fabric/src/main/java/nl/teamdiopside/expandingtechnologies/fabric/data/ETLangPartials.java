package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.google.gson.JsonElement;
import com.simibubi.create.foundation.data.LangPartial;
import com.simibubi.create.foundation.utility.Lang;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

import java.util.function.Supplier;

public enum ETLangPartials implements LangPartial {

    INTERFACE("UI & Messages"),
    SUBTITLES("Subtitles"),
    PONDER("Ponder Content"),
    ;

    private final String display;
    private final Supplier<JsonElement> provider;

    ETLangPartials(String displayName) {
        this.display = displayName;
        String fileName = Lang.asId(name());
        this.provider = () -> LangPartial.fromResource(ExpandingTechnologies.MODID, fileName);
    }

    @Override
    public String getDisplayName() {
        return this.display;
    }

    @Override
    public JsonElement provide() {
        return this.provider.get();
    }
}
