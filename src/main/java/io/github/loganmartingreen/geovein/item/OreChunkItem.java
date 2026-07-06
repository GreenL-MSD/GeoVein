package io.github.loganmartingreen.geovein.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OreChunkItem extends Item {
    public OreChunkItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag
    ) {
        tooltip.add(Component.literal("Ore: Unknown").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Grade: Unsorted").withStyle(ChatFormatting.GRAY));
    }
}