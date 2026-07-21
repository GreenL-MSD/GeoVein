package io.github.loganmartingreen.geovein;

import io.github.loganmartingreen.geovein.item.OreBilletItem;
import org.slf4j.Logger;
import io.github.loganmartingreen.geovein.data.ConfigResourcePackLoader;
import com.mojang.logging.LogUtils;

import io.github.loganmartingreen.geovein.ore.OreChunkDropHandler;
import io.github.loganmartingreen.geovein.worldgen.ModFeatures;
import io.github.loganmartingreen.geovein.command.GeoVeinCommands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import io.github.loganmartingreen.geovein.data.OreDefinitionReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.item.OreChunkItem;
import io.github.loganmartingreen.geovein.ore.OreGrade;
import io.github.loganmartingreen.geovein.component.ModDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import io.github.loganmartingreen.geovein.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GeoVein.MODID)
public class GeoVein {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "geovein";
    // Directly reference a logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GEOVEIN_TAB = CREATIVE_MODE_TABS.register("geovein_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.geovein")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.ORE_CHUNK.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (OreDefinition ore : OreDefinitionLoader.getLoadedDefinitions()) {
                    for (OreGrade grade : OreGrade.values()) {
                        output.accept(OreChunkItem.create(ore.id(), grade));
                    }

                    output.accept(OreBilletItem.create(ore.id()));
                }
            }).build());
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GeoVein(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModFeatures.register(modEventBus);
        modEventBus.addListener(ConfigResourcePackLoader::addPackFinders);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new OreChunkDropHandler());
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new OreDefinitionReloadListener());
    }
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        GeoVeinCommands.register(event.getDispatcher());
    }
}
