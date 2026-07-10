package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record OreDropSettings(
        int minChunksPerBlock,
        int maxChunksPerBlock,
        List<OreGradeBand> gradeBands
) {
    public static final Codec<OreDropSettings> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .optionalFieldOf("min_chunks_per_block", 4)
                            .forGetter(OreDropSettings::minChunksPerBlock),

                    Codec.INT
                            .optionalFieldOf("max_chunks_per_block", 4)
                            .forGetter(OreDropSettings::maxChunksPerBlock),

                    OreGradeBand.CODEC
                            .listOf()
                            .optionalFieldOf("grade_bands", OreGradeBand.defaultBands())
                            .forGetter(OreDropSettings::gradeBands)
            ).apply(instance, OreDropSettings::new));

    public static OreDropSettings defaultSettings() {
        return new OreDropSettings(
                4,
                4,
                OreGradeBand.defaultBands()
        );
    }
}