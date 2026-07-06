package io.github.loganmartingreen.geovein.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record OreDefinition(
        String id,
        String displayName,
        String sourceOreBlock
) {
    public static final Codec<OreDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(OreDefinition::id),
            Codec.STRING.fieldOf("display_name").forGetter(OreDefinition::displayName),
            Codec.STRING.fieldOf("source_ore_block").forGetter(OreDefinition::sourceOreBlock)
    ).apply(instance, OreDefinition::new));
}