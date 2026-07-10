package io.github.loganmartingreen.geovein.item;

import io.github.loganmartingreen.geovein.GeoVein;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(GeoVein.MODID);

    public static final Supplier<OreChunkItem> ORE_CHUNK =
            ITEMS.register("ore_chunk", () -> new OreChunkItem(new Item.Properties()));

    public static final DeferredItem<Item> ORE_BILLET = ITEMS.register(
            "ore_billet",
            () -> new OreBilletItem(new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}