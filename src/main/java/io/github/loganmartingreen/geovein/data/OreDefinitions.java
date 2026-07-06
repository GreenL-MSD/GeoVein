package io.github.loganmartingreen.geovein.data;

import java.util.List;
import java.util.Optional;

public class OreDefinitions {
    public static final List<OreDefinition> DEFINITIONS = List.of(
            new OreDefinition("copper", "Copper", "minecraft:copper_ore"),
            new OreDefinition("iron", "Iron", "minecraft:iron_ore"),
            new OreDefinition("gold", "Gold", "minecraft:gold_ore"),
            new OreDefinition("coal", "Coal", "minecraft:coal_ore")
    );

    public static Optional<OreDefinition> getById(String id) {
        for (OreDefinition definition : DEFINITIONS) {
            if (definition.id().equals(id)) {
                return Optional.of(definition);
            }
        }

        return Optional.empty();
    }

    public static String getDisplayName(String id) {
        return getById(id)
                .map(OreDefinition::displayName)
                .orElse(id);
    }
}