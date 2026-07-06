package io.github.loganmartingreen.geovein.component;

import net.minecraft.core.registries.Registries;
import com.mojang.serialization.Codec;
import io.github.loganmartingreen.geovein.GeoVein;
import io.github.loganmartingreen.geovein.ore.OreGrade;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GeoVein.MODID);

    public static final Codec<OreGrade> ORE_GRADE_CODEC =
            Codec.STRING.xmap(OreGrade::valueOf, OreGrade::name);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreGrade>> ORE_GRADE =
            DATA_COMPONENTS.registerComponentType("ore_grade", builder ->
                    builder.persistent(ORE_GRADE_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ORE_ID =
            DATA_COMPONENTS.registerComponentType("ore_id", builder ->
                    builder.persistent(Codec.STRING)
            );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}