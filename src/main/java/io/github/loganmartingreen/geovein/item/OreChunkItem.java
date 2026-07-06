package io.github.loganmartingreen.geovein.item;

import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.component.ModDataComponents;
import io.github.loganmartingreen.geovein.ore.OreGrade;
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

    public static ItemStack create(String oreId, OreGrade grade) {
        ItemStack stack = new ItemStack(ModItems.ORE_CHUNK.get());

        stack.set(ModDataComponents.ORE_ID.get(), oreId);
        stack.set(ModDataComponents.ORE_GRADE.get(), grade);

        return stack;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String oreId = stack.getOrDefault(ModDataComponents.ORE_ID.get(), "unknown");
        OreGrade grade = stack.getOrDefault(ModDataComponents.ORE_GRADE.get(), OreGrade.COMMON);

        String oreName = OreDefinitionLoader.getDisplayName(oreId);

        return Component.literal(grade.getDisplayName() + " " + oreName + " Ore Chunk");
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag
    ) {
        String oreId = stack.getOrDefault(ModDataComponents.ORE_ID.get(), "Unknown");
        OreGrade grade = stack.getOrDefault(ModDataComponents.ORE_GRADE.get(), OreGrade.COMMON);

        tooltip.add(Component.literal("Ore: " + OreDefinitionLoader.getDisplayName(oreId)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Grade: " + grade.getDisplayName()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Yield: x" + grade.getYieldMultiplier()).withStyle(ChatFormatting.DARK_GRAY));
    }
}