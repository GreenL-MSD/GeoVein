package io.github.loganmartingreen.geovein.data;

import java.util.ArrayList;
import java.util.List;

public class OreDefinitionLoader {
    private static final List<OreDefinition> LOADED_DEFINITIONS = new ArrayList<>();

    public static void loadDefaultsForNow() {
        LOADED_DEFINITIONS.clear();
        LOADED_DEFINITIONS.addAll(OreDefinitions.DEFINITIONS);
    }

    public static List<OreDefinition> getLoadedDefinitions() {
        return List.copyOf(LOADED_DEFINITIONS);
    }
}