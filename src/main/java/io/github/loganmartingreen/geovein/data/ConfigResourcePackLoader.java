package io.github.loganmartingreen.geovein.data;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class ConfigResourcePackLoader {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Path RESOURCE_PACK_DIR =
            ConfigOreDefinitionLoader.GEOVEIN_CONFIG_DIR.resolve("resource_pack");

    private ConfigResourcePackLoader() {
    }

    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) {
            return;
        }

        ensureResourcePackFolder();

        PackLocationInfo locationInfo = new PackLocationInfo(
                "geovein_config_resources",
                Component.literal("GeoVein Config Resources"),
                PackSource.BUILT_IN,
                Optional.empty()
        );

        PackSelectionConfig selectionConfig = new PackSelectionConfig(
                true,
                Pack.Position.TOP,
                false
        );

        Pack pack = Pack.readMetaAndCreate(
                locationInfo,
                new PathPackResources.PathResourcesSupplier(RESOURCE_PACK_DIR),
                PackType.CLIENT_RESOURCES,
                selectionConfig
        );

        if (pack == null) {
            LOGGER.warn("GeoVein config resource pack could not be created from {}", RESOURCE_PACK_DIR);
            return;
        }

        event.addRepositorySource(consumer -> consumer.accept(pack));
        LOGGER.info("Registered GeoVein config resource pack from {}", RESOURCE_PACK_DIR);
    }

    public static void ensureResourcePackFolder() {
        try {
            Files.createDirectories(RESOURCE_PACK_DIR);

            Files.createDirectories(RESOURCE_PACK_DIR.resolve("assets/geovein/textures/item/ore_chunks"));
            Files.createDirectories(RESOURCE_PACK_DIR.resolve("assets/geovein/textures/item/ore_billets"));

            Files.createDirectories(RESOURCE_PACK_DIR.resolve("assets/geovein/models/item/ore_chunks"));
            Files.createDirectories(RESOURCE_PACK_DIR.resolve("assets/geovein/models/item/ore_billets"));

            writePackMcmeta();
            writeReadme();
        } catch (Exception exception) {
            LOGGER.error("Failed to create GeoVein config resource pack folder", exception);
        }
    }

    private static void writePackMcmeta() {
        Path packMetaPath = RESOURCE_PACK_DIR.resolve("pack.mcmeta");

        if (Files.exists(packMetaPath)) {
            return;
        }

        String packMeta = """
                {
                  "pack": {
                    "pack_format": 34,
                    "description": "GeoVein Config Resource Pack"
                  }
                }
                """;

        try {
            Files.writeString(packMetaPath, packMeta);
        } catch (Exception exception) {
            LOGGER.error("Failed to write GeoVein config resource pack.mcmeta", exception);
        }
    }

    private static void writeReadme() {
        Path readmePath = RESOURCE_PACK_DIR.resolve("README.txt");

        if (Files.exists(readmePath)) {
            return;
        }

        String readme = """
                GeoVein Config Resource Pack

                This folder is automatically loaded by GeoVein.

                Use this folder for custom textures and item models.

                Example structure:

                config/geovein/resource_pack/
                └─ assets/
                   └─ geovein/
                      ├─ textures/
                      │  └─ item/
                      │     ├─ ore_chunks/
                      │     │  └─ emerald/
                      │     │     ├─ poor.png
                      │     │     ├─ common.png
                      │     │     ├─ rich.png
                      │     │     └─ native.png
                      │     └─ ore_billets/
                      │        └─ emerald.png
                      └─ models/
                         └─ item/
                            ├─ ore_chunk.json
                            ├─ ore_billet.json
                            ├─ ore_chunks/
                            │  └─ emerald/
                            │     ├─ poor.json
                            │     ├─ common.json
                            │     ├─ rich.json
                            │     └─ native.json
                            └─ ore_billets/
                               └─ emerald.json

                To add a new ore texture, you still need:
                - PNG textures
                - item model JSON files
                - ore_chunk.json custom_model_data overrides
                - ore_billet.json custom_model_data override

                This resource pack is client-side.
                Ore definitions still go in:

                config/geovein/ore_definitions/
                """;

        try {
            Files.writeString(readmePath, readme);
        } catch (Exception exception) {
            LOGGER.error("Failed to write GeoVein config resource pack README", exception);
        }
    }
}