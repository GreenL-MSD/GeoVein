package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.loganmartingreen.geovein.worldgen.VeinShape;

public record OreDefinition(
        String id,
        String displayName,
        String sourceOreBlock,
        String deepslateOreBlock,
        int deepslateBelowY,
        VeinShape shape,
        int minY,
        int maxY,
        int length,
        int width,
        int height,
        double density,
        int regionSizeChunks,
        double spawnChancePerRegion,
        OreDropSettings drops
) {
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
                            .optionalFieldOf("deepslate_ore_block", "")
                            .forGetter(OreDefinition::deepslateOreBlock),

                    Codec.INT
                            .optionalFieldOf("deepslate_below_y", 0)
                            .forGetter(OreDefinition::deepslateBelowY),

                    VeinShape.CODEC
                            .optionalFieldOf("shape", VeinShape.ELLIPSOID)
                            .forGetter(OreDefinition::shape),

                    Codec.INT
                            .optionalFieldOf("min_y", -48)
                            .forGetter(OreDefinition::minY),

                    Codec.INT
                            .optionalFieldOf("max_y", 32)
                            .forGetter(OreDefinition::maxY),

                    Codec.INT
                            .optionalFieldOf("length", 240)
                            .forGetter(OreDefinition::length),

                    Codec.INT
                            .optionalFieldOf("width", 80)
                            .forGetter(OreDefinition::width),

                    Codec.INT
                            .optionalFieldOf("height", 40)
                            .forGetter(OreDefinition::height),

                    Codec.DOUBLE
                            .optionalFieldOf("density", 0.30)
                            .forGetter(OreDefinition::density),

                    Codec.INT
                            .optionalFieldOf("region_size_chunks", 32)
                            .forGetter(OreDefinition::regionSizeChunks),

                    Codec.DOUBLE
                            .optionalFieldOf("spawn_chance_per_region", 0.30)
                            .forGetter(OreDefinition::spawnChancePerRegion),

                    OreDropSettings.CODEC
                            .optionalFieldOf("drops", OreDropSettings.defaultSettings())
                            .forGetter(OreDefinition::drops)
            ).apply(instance, OreDefinition::new));
}