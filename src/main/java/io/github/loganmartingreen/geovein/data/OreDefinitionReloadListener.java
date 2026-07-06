package io.github.loganmartingreen.geovein.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OreDefinitionReloadListener implements ResourceManagerReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String DIRECTORY = "ore_definitions";

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        List<OreDefinition> definitions = new ArrayList<>();

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                DIRECTORY,
                location -> location.getPath().endsWith(".json")
        );

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation location = entry.getKey();
            Resource resource = entry.getValue();

            try (
                    InputStream stream = resource.open();
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
            ) {
                JsonElement json = JsonParser.parseReader(reader);

                OreDefinition.CODEC.parse(JsonOps.INSTANCE, json)
                        .resultOrPartial(error -> LOGGER.error("Failed to parse ore definition {}: {}", location, error))
                        .ifPresent(definitions::add);
            } catch (Exception exception) {
                LOGGER.error("Failed to load ore definition: {}", location, exception);
            }
        }

        OreDefinitionLoader.replaceLoadedDefinitions(definitions);
    }
}