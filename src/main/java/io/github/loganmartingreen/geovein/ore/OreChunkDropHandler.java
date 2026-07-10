package io.github.loganmartingreen.geovein.ore;

import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.item.OreChunkItem;
import io.github.loganmartingreen.geovein.worldgen.Deposit;
import io.github.loganmartingreen.geovein.worldgen.DepositGenerator;
import io.github.loganmartingreen.geovein.worldgen.OreGradeCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.Optional;

public class OreChunkDropHandler {
    @SubscribeEvent
    public void onBlockDrops(BlockDropsEvent event) {
        ServerLevel level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        Optional<OreDefinition> oreOptional = findMatchingOreDefinition(state);

        if (oreOptional.isEmpty()) {
            return;
        }

        OreDefinition ore = oreOptional.get();

        Optional<Deposit> depositOptional = findDepositContainingPosition(
                ore,
                level.getSeed(),
                pos
        );

        if (depositOptional.isEmpty()) {
            return;
        }

        Deposit deposit = depositOptional.get();
        OreGrade grade = OreGradeCalculator.calculateGrade(deposit, pos);

        ItemStack oreChunk = OreChunkItem.create(ore.id(), grade);

        event.getDrops().clear();

        event.getDrops().add(
                new ItemEntity(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        oreChunk
                )
        );
    }

    private Optional<OreDefinition> findMatchingOreDefinition(BlockState state) {
        String brokenBlockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();

        for (OreDefinition ore : OreDefinitionLoader.getLoadedDefinitions()) {
            if (brokenBlockId.equals(ore.sourceOreBlock())) {
                return Optional.of(ore);
            }

            if (!ore.deepslateOreBlock().isBlank() && brokenBlockId.equals(ore.deepslateOreBlock())) {
                return Optional.of(ore);
            }
        }

        return Optional.empty();
    }

    private Optional<Deposit> findDepositContainingPosition(
            OreDefinition ore,
            long worldSeed,
            BlockPos pos
    ) {
        int regionSizeBlocks = ore.regionSizeChunks() * 16;

        int regionX = Math.floorDiv(pos.getX(), regionSizeBlocks);
        int regionZ = Math.floorDiv(pos.getZ(), regionSizeBlocks);

        int largestDepositRadius = Math.max(
                ore.length(),
                Math.max(ore.width(), ore.height())
        ) / 2;

        int searchRadiusRegions = Math.max(
                1,
                (int) Math.ceil((double) largestDepositRadius / regionSizeBlocks) + 1
        );

        for (int checkRegionX = regionX - searchRadiusRegions; checkRegionX <= regionX + searchRadiusRegions; checkRegionX++) {
            for (int checkRegionZ = regionZ - searchRadiusRegions; checkRegionZ <= regionZ + searchRadiusRegions; checkRegionZ++) {
                Optional<Deposit> depositOptional = DepositGenerator.generateForRegion(
                        ore,
                        worldSeed,
                        checkRegionX,
                        checkRegionZ
                );

                if (depositOptional.isEmpty()) {
                    continue;
                }

                Deposit deposit = depositOptional.get();

                if (deposit.contains(pos)) {
                    return Optional.of(deposit);
                }
            }
        }

        return Optional.empty();
    }
}