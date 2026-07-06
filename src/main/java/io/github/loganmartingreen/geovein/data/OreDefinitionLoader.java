package io.github.loganmartingreen.geovein.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OreDefinitionLoader {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<OreDefinition> LOADED_DEFINITIONS = new ArrayList<>();

    private static final List<String> BUILT_IN_ORE_FILES = List.of(
            "copper",
            "iron",
            "gold",
            "coal"
    );

    public static void loadDefaultsForNow() {
        LOADED_DEFINITIONS.clear();

        for (String oreId : BUILT_IN_ORE_FILES) {
            loadBuiltInDefinition(oreId);
        }

        LOGGER.info("Loaded {} GeoVein ore definitions", LOADED_DEFINITIONS.size());
    }

    private static void loadBuiltInDefinition(String oreId) {
        String path = "/data/geovein/ore_definitions/" + oreId + ".json";

        try (InputStream stream = OreDefinitionLoader.class.getResourceAsStream(path)) {
            if (stream == null) {
                LOGGER.error("Could not find built-in ore definition: {}", path);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonElement json = JsonParser.parseReader(reader);

                OreDefinition.CODEC.parse(JsonOps.INSTANCE, json)
                        .resultOrPartial(error -> LOGGER.error("Failed to parse ore definition {}: {}", path, error))
                        .ifPresent(LOADED_DEFINITIONS::add);
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to load ore definition: {}", path, exception);
        }
    }

    public static void replaceLoadedDefinitions(List<OreDefinition> definitions) {
        LOADED_DEFINITIONS.clear();
        LOADED_DEFINITIONS.addAll(definitions);

        LOGGER.info("Loaded {} GeoVein ore definitions", LOADED_DEFINITIONS.size());
    }

    public static List<OreDefinition> getLoadedDefinitions() {
        return List.copyOf(LOADED_DEFINITIONS);
    }
}