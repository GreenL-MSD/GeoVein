package io.github.loganmartingreen.geovein.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.component.ModDataComponents;
import io.github.loganmartingreen.geovein.ore.OreGrade;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;

import java.util.List;

public class OreChunkItem extends Item {
    public OreChunkItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(String oreId, OreGrade grade) {
        ItemStack stack = new ItemStack(ModItems.ORE_CHUNK.get());

        stack.set(ModDataComponents.ORE_ID.get(), oreId);
        stack.set(ModDataComponents.ORE_GRADE.get(), grade);
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(getCustomModelData(oreId, grade)));

        return stack;
    }

    private static int getCustomModelData(String oreId, OreGrade grade) {
        int base = OreDefinitionLoader.getById(oreId)
                .map(OreDefinition::chunkModelDataBase)
                .orElse(9000);

        int gradeIndex = switch (grade) {
            case POOR -> 0;
            case COMMON -> 1;
            case RICH -> 2;
            case NATIVE -> 3;
        };

        return base + gradeIndex;
    }

    private static int getGradeIndex(OreGrade grade) {
        return switch (grade) {
            case POOR -> 0;
            case COMMON -> 1;
            case RICH -> 2;
            case NATIVE -> 3;
        };
    }

    private static String cleanOreIdForPath(String oreId) {
        if (oreId.contains(":")) {
            return oreId.split(":", 2)[1].toLowerCase();
        }

        return oreId.toLowerCase();
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