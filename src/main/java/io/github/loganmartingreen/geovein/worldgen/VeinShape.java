package io.github.loganmartingreen.geovein.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Locale;

public enum VeinShape {
    ELLIPSOID,
    SPHERE,
    CONE,
    SHEET,
    PIPE;

    public static final Codec<VeinShape> CODEC = Codec.STRING.comapFlatMap(
            value -> {
                try {
                    return DataResult.success(
                            VeinShape.valueOf(value.toUpperCase(Locale.ROOT))
                    );
                } catch (IllegalArgumentException exception) {
                    return DataResult.error(
                            () -> "Unknown GeoVein shape: " + value
                    );
                }
            },
            shape -> shape.name().toLowerCase(Locale.ROOT)
    );
}