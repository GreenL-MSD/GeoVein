package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record OreModelSettings(
        int chunkModelDataBase,
        int billetModelData
) {
    public static final MapCodec<OreModelSettings> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT
                            .optionalFieldOf("chunk_model_data_base", 9000)
                            .forGetter(OreModelSettings::chunkModelDataBase),

                    Codec.INT
                            .optionalFieldOf("billet_model_data", 9100)
                            .forGetter(OreModelSettings::billetModelData)
            ).apply(instance, OreModelSettings::new));
}