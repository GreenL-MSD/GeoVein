package io.github.loganmartingreen.geovein.item;

import io.github.loganmartingreen.geovein.component.ModDataComponents;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomModelData;
import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import net.minecraft.core.component.DataComponents;

import java.util.List;

public class OreBilletItem extends Item {
    public OreBilletItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(String oreId) {
        ItemStack stack = new ItemStack(ModItems.ORE_BILLET.get());

        stack.set(ModDataComponents.ORE_ID.get(), oreId);
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(getCustomModelData(oreId)));

        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        String oreId = stack.getOrDefault(ModDataComponents.ORE_ID.get(), "unknown");
        String oreName = OreDefinitionLoader.getDisplayName(oreId);

        return Component.literal(oreName + " Ore Billet");
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        String oreId = stack.getOrDefault(ModDataComponents.ORE_ID.get(), "unknown");
        String oreName = OreDefinitionLoader.getDisplayName(oreId);

        tooltip.add(Component.literal("Ore: " + oreName));

        super.appendHoverText(stack, context, tooltip, flag);
    }

    private static int getCustomModelData(String oreId) {
        return OreDefinitionLoader.getById(oreId)
                .map(OreDefinition::billetModelData)
                .orElse(9100);
    }
}