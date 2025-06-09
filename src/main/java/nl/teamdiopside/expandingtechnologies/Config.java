package nl.teamdiopside.expandingtechnologies;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = ExpandingTechnologies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ALLOW_SELF_ADDRESS = BUILDER.comment("Whether '@s' should be substituted for the player's name when ordering a package at a shopkeeper").define("allowSelfAddress", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean allowSelfAddress;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        allowSelfAddress = ALLOW_SELF_ADDRESS.get();
    }
}
