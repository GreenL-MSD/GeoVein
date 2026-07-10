package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record OreGradeBand(
        double maxDistance,
        double poorChance,
        double commonChance,
        double richChance,
        double nativeChance
) {
    public static final Codec<OreGradeBand> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.DOUBLE
                            .fieldOf("max_distance")
                            .forGetter(OreGradeBand::maxDistance),

                    Codec.DOUBLE
                            .optionalFieldOf("poor", 0.0)
                            .forGetter(OreGradeBand::poorChance),

                    Codec.DOUBLE
                            .optionalFieldOf("common", 0.0)
                            .forGetter(OreGradeBand::commonChance),

                    Codec.DOUBLE
                            .optionalFieldOf("rich", 0.0)
                            .forGetter(OreGradeBand::richChance),

                    Codec.DOUBLE
                            .optionalFieldOf("native", 0.0)
                            .forGetter(OreGradeBand::nativeChance)
            ).apply(instance, OreGradeBand::new));

    public static List<OreGradeBand> defaultBands() {
        return List.of(
                new OreGradeBand(0.25, 0.0833, 0.0833, 0.0834, 0.75),
                new OreGradeBand(0.50, 0.0667, 0.0667, 0.80, 0.0666),
                new OreGradeBand(0.75, 0.0333, 0.90, 0.0334, 0.0333),
                new OreGradeBand(1.00, 1.00, 0.00, 0.00, 0.00)
        );
    }
}