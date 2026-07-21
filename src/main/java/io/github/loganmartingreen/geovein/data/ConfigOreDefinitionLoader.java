package io.github.loganmartingreen.geovein.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class ConfigOreDefinitionLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path GEOVEIN_CONFIG_DIR =
            FMLPaths.CONFIGDIR.get().resolve("geovein");

    public static final Path ORE_DEFINITION_DIR =
            GEOVEIN_CONFIG_DIR.resolve("ore_definitions");

    private ConfigOreDefinitionLoader() {
    }

    public static void exportMissingDefaults(Collection<OreDefinition> definitions) {
        try {
            Files.createDirectories(ORE_DEFINITION_DIR);
            writeReadme();

            for (OreDefinition definition : definitions) {
                Path outputPath = ORE_DEFINITION_DIR.resolve(definition.id() + ".json");

                if (Files.exists(outputPath)) {
                    continue;
                }

                OreDefinition.CODEC
                        .encodeStart(JsonOps.INSTANCE, definition)
                        .resultOrPartial(error -> LOGGER.error(
                                "Failed to export default GeoVein ore definition {}: {}",
                                definition.id(),
                                error
                        ))
                        .ifPresent(json -> writeJson(outputPath, json));
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to export GeoVein config ore definitions", exception);
        }
    }

    public static List<OreDefinition> loadConfigDefinitions() {
        List<OreDefinition> definitions = new ArrayList<>();

        try {
            Files.createDirectories(ORE_DEFINITION_DIR);

            try (Stream<Path> paths = Files.list(ORE_DEFINITION_DIR)) {
                paths
                        .filter(path -> path.toString().endsWith(".json"))
                        .sorted()
                        .forEach(path -> readDefinition(path, definitions));
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to load GeoVein config ore definitions", exception);
        }

        return definitions;
    }

    private static void readDefinition(Path path, List<OreDefinition> definitions) {
        try (Reader reader = Files.newBufferedReader(path)) {
            JsonElement json = JsonParser.parseReader(reader);

            OreDefinition.CODEC
                    .parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(error -> LOGGER.error(
                            "Failed to parse GeoVein config ore definition {}: {}",
                            path,
                            error
                    ))
                    .ifPresent(definitions::add);
        } catch (Exception exception) {
            LOGGER.error("Failed to read GeoVein config ore definition {}", path, exception);
        }
    }

    private static void writeJson(Path path, JsonElement json) {
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(json, writer);
            LOGGER.info("Exported default GeoVein ore config: {}", path);
        } catch (Exception exception) {
            LOGGER.error("Failed to write GeoVein ore config {}", path, exception);
        }
    }

    private static void writeReadme() {
        Path readmePath = GEOVEIN_CONFIG_DIR.resolve("README.txt");

        if (Files.exists(readmePath)) {
            return;
        }

        String readme = """
                GeoVein Config Folder

                Ore and deposit definitions are loaded from:

                config/geovein/ore_definitions/

                Each JSON file defines one ore deposit type.

                You can edit existing files or add new files.

                Changes require /reload or a world restart.

                Important:
                - source_ore_block must be a real block ID.
                - processed_item must be a real item ID.
                - chunk_model_data_base controls ore chunk textures.
                - billet_model_data controls ore billet textures.

                Textures and models are handled separately because Minecraft loads them as assets.
                """;

        try {
            Files.createDirectories(GEOVEIN_CONFIG_DIR);
            Files.writeString(readmePath, readme);
        } catch (Exception exception) {
            LOGGER.error("Failed to write GeoVein config README", exception);
        }
    }
}