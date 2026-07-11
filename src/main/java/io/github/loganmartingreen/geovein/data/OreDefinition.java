package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.loganmartingreen.geovein.worldgen.VeinShape;

public record OreDefinition(
        String id,
        String displayName,
        String sourceOreBlock,
        String processedItem,
        String deepslateOreBlock,
        int deepslateBelowY,

        OreModelSettings modelSettings,
        OreGenerationSettings generationSettings,

        OreDropSettings drops
) {
    public int chunkModelDataBase() {
        return modelSettings.chunkModelDataBase();
    }

    public int billetModelData() {
        return modelSettings.billetModelData();
    }

    public VeinShape shape() {
        return generationSettings.shape();
    }

    public int minY() {
        return generationSettings.minY();
    }

    public int maxY() {
        return generationSettings.maxY();
    }

    public int length() {
        return generationSettings.length();
    }

    public int width() {
        return generationSettings.width();
    }

    public int height() {
        return generationSettings.height();
    }

    public double density() {
        return generationSettings.density();
    }

    public int regionSizeChunks() {
        return generationSettings.regionSizeChunks();
    }

    public double spawnChancePerRegion() {
        return generationSettings.spawnChancePerRegion();
    }

    public static final Codec<OreDefinition> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING
                            .fieldOf("id")
                            .forGetter(OreDefinition::id),

                    Codec.STRING
                            .fieldOf("display_name")
                            .forGetter(OreDefinition::displayName),

                    Codec.STRING
                            .fieldOf("source_ore_block")
                            .forGetter(OreDefinition::sourceOreBlock),

                    Codec.STRING
                            .optionalFieldOf("processed_item", "")
                            .forGetter(OreDefinition::processedItem),

                    Codec.STRING
                            .optionalFieldOf("deepslate_ore_block", "")
                            .forGetter(OreDefinition::deepslateOreBlock),

                    Codec.INT
                            .optionalFieldOf("deepslate_below_y", 0)
                            .forGetter(OreDefinition::deepslateBelowY),

                    OreModelSettings.MAP_CODEC
                            .forGetter(OreDefinition::modelSettings),

                    OreGenerationSettings.MAP_CODEC
                            .forGetter(OreDefinition::generationSettings),

                    OreDropSettings.CODEC
                            .optionalFieldOf("drops", OreDropSettings.defaultSettings())
                            .forGetter(OreDefinition::drops)
            ).apply(instance, OreDefinition::new));
}