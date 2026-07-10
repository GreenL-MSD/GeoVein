package io.github.loganmartingreen.geovein.ore;

import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.data.OreGradeBand;
import io.github.loganmartingreen.geovein.item.OreChunkItem;
import io.github.loganmartingreen.geovein.worldgen.Deposit;
import io.github.loganmartingreen.geovein.worldgen.DepositGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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

        double distance = deposit.getNormalizedDistanceFromCore(pos);
        Random random = new Random();

        int chunkCount = getChunkDropCount(ore, random);

        Map<OreGrade, Integer> chunksByGrade = new EnumMap<>(OreGrade.class);

        for (int i = 0; i < chunkCount; i++) {
            OreGrade grade = rollGrade(ore, distance, random);
            chunksByGrade.merge(grade, 1, Integer::sum);
        }

        event.getDrops().clear();

        for (Map.Entry<OreGrade, Integer> entry : chunksByGrade.entrySet()) {
            ItemStack oreChunk = OreChunkItem.create(ore.id(), entry.getKey());
            oreChunk.setCount(entry.getValue());

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
    }

    private int getChunkDropCount(OreDefinition ore, Random random) {
        int min = Math.max(0, ore.drops().minChunksPerBlock());
        int max = Math.max(min, ore.drops().maxChunksPerBlock());

        if (min == max) {
            return min;
        }

        return min + random.nextInt(max - min + 1);
    }

    private OreGrade rollGrade(OreDefinition ore, double distance, Random random) {
        OreGradeBand band = getGradeBandForDistance(ore, distance);

        double poor = Math.max(0.0, band.poorChance());
        double common = Math.max(0.0, band.commonChance());
        double rich = Math.max(0.0, band.richChance());
        double nativeOre = Math.max(0.0, band.nativeChance());

        double total = poor + common + rich + nativeOre;

        if (total <= 0.0) {
            return OreGrade.POOR;
        }

        double roll = random.nextDouble() * total;

        if (roll < poor) {
            return OreGrade.POOR;
        }

        roll -= poor;

        if (roll < common) {
            return OreGrade.COMMON;
        }

        roll -= common;

        if (roll < rich) {
            return OreGrade.RICH;
        }

        return OreGrade.NATIVE;
    }

    private OreGradeBand getGradeBandForDistance(OreDefinition ore, double distance) {
        for (OreGradeBand band : ore.drops().gradeBands()) {
            if (distance <= band.maxDistance()) {
                return band;
            }
        }

        return OreGradeBand.defaultBands().get(OreGradeBand.defaultBands().size() - 1);
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