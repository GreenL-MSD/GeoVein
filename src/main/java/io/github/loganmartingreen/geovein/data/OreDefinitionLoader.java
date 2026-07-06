package io.github.loganmartingreen.geovein.data;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class OreDefinitionLoader {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<OreDefinition> LOADED_DEFINITIONS = new ArrayList<>();

    public static void replaceLoadedDefinitions(List<OreDefinition> definitions) {
        LOADED_DEFINITIONS.clear();
        LOADED_DEFINITIONS.addAll(definitions);

        LOGGER.info("Loaded {} GeoVein ore definitions", LOADED_DEFINITIONS.size());
    }

    public static List<OreDefinition> getLoadedDefinitions() {
        return List.copyOf(LOADED_DEFINITIONS);
    }
}