package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.loganmartingreen.geovein.worldgen.VeinShape;

public record OreGenerationSettings(
        VeinShape shape,
        int minY,
        int maxY,
        int length,
        int width,
        int height,
        double density,
        int regionSizeChunks,
        double spawnChancePerRegion
) {
    public static final MapCodec<OreGenerationSettings> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    VeinShape.CODEC
                            .optionalFieldOf("shape", VeinShape.ELLIPSOID)
                            .forGetter(OreGenerationSettings::shape),

                    Codec.INT
                            .optionalFieldOf("min_y", -48)
                            .forGetter(OreGenerationSettings::minY),

                    Codec.INT
                            .optionalFieldOf("max_y", 32)
                            .forGetter(OreGenerationSettings::maxY),

                    Codec.INT
                            .optionalFieldOf("length", 240)
                            .forGetter(OreGenerationSettings::length),

                    Codec.INT
                            .optionalFieldOf("width", 80)
                            .forGetter(OreGenerationSettings::width),

                    Codec.INT
                            .optionalFieldOf("height", 40)
                            .forGetter(OreGenerationSettings::height),

                    Codec.DOUBLE
                            .optionalFieldOf("density", 0.30)
                            .forGetter(OreGenerationSettings::density),

                    Codec.INT
                            .optionalFieldOf("region_size_chunks", 32)
                            .forGetter(OreGenerationSettings::regionSizeChunks),

                    Codec.DOUBLE
                            .optionalFieldOf("spawn_chance_per_region", 0.30)
                            .forGetter(OreGenerationSettings::spawnChancePerRegion)
            ).apply(instance, OreGenerationSettings::new));
}