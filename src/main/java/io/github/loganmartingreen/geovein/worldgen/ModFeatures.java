package io.github.loganmartingreen.geovein.worldgen;

import io.github.loganmartingreen.geovein.GeoVein;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, GeoVein.MODID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GEOVEIN_DEPOSIT =
            FEATURES.register(
                    "geovein_deposit",
                    () -> new GeoVeinDepositFeature(NoneFeatureConfiguration.CODEC)
            );

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}