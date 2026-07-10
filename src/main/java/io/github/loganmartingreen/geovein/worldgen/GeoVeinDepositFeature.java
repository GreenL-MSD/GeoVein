package io.github.loganmartingreen.geovein.worldgen;

import com.mojang.serialization.Codec;
import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;
import java.util.Random;

public class GeoVeinDepositFeature extends Feature<NoneFeatureConfiguration> {
    public GeoVeinDepositFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        ChunkPos chunkPos = new ChunkPos(origin);

        long worldSeed = level.getSeed();

        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();

        int chunkCenterX = chunkMinX + 8;
        int chunkCenterZ = chunkMinZ + 8;

        int totalPlaced = 0;

        for (OreDefinition ore : OreDefinitionLoader.getLoadedDefinitions()) {
            int regionSizeBlocks = ore.regionSizeChunks() * 16;

            int regionX = Math.floorDiv(chunkCenterX, regionSizeBlocks);
            int regionZ = Math.floorDiv(chunkCenterZ, regionSizeBlocks);

            for (int checkRegionX = regionX - 1; checkRegionX <= regionX + 1; checkRegionX++) {
                for (int checkRegionZ = regionZ - 1; checkRegionZ <= regionZ + 1; checkRegionZ++) {
                    Optional<Deposit> depositOptional = DepositGenerator.generateForRegion(
                            ore,
                            worldSeed,
                            checkRegionX,
                            checkRegionZ
                    );

                    if (depositOptional.isEmpty()) {
                        continue;
                    }

                    totalPlaced += placeDepositInChunk(
                            level,
                            depositOptional.get(),
                            chunkMinX,
                            chunkMinZ
                    );
                }
            }
        }

        return totalPlaced > 0;
    }

    private int placeDepositInChunk(
            WorldGenLevel level,
            Deposit deposit,
            int chunkMinX,
            int chunkMinZ
    ) {
        int yRadius = Math.max(
                deposit.length(),
                Math.max(deposit.width(), deposit.height())
        ) / 2;

        int minY = Mth.clamp(
                deposit.center().getY() - yRadius,
                level.getMinBuildHeight(),
                level.getMaxBuildHeight() - 1
        );

        int maxY = Mth.clamp(
                deposit.center().getY() + yRadius,
                level.getMinBuildHeight(),
                level.getMaxBuildHeight() - 1
        );

        int placed = 0;

        for (int x = chunkMinX; x < chunkMinX + 16; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!deposit.contains(pos)) {
                        continue;
                    }

                    BlockState currentState = level.getBlockState(pos);

                    if (!shouldReplace(currentState)) {
                        continue;
                    }

                    double distance = deposit.getNormalizedDistanceFromCore(pos);
                    double density = calculateDensity(deposit, distance);

                    double noise = getDeterministicNoise(deposit.seed(), pos);

                    if (noise > density) {
                        continue;
                    }

                    Optional<Block> oreBlockOptional = getOreBlockForY(deposit.ore(), pos.getY());

                    if (oreBlockOptional.isEmpty()) {
                        continue;
                    }

                    level.setBlock(pos, oreBlockOptional.get().defaultBlockState(), 2);
                    placed++;
                }
            }
        }

        return placed;
    }

    private double calculateDensity(Deposit deposit, double distance) {
        if (distance <= 0.25) {
            return 1.0;
        }

        double fadeProgress = (distance - 0.25) / 0.75;

        double edgeDensity = Math.max(0.15, Math.min(1.0, deposit.ore().density()));

        double density = 1.0 - fadeProgress * (1.0 - edgeDensity);

        return Math.max(0.0, Math.min(1.0, density));
    }

    private double getDeterministicNoise(long seed, BlockPos pos) {
        long mixedSeed = seed
                ^ ((long) pos.getX() * 341873128712L)
                ^ ((long) pos.getY() * 132897987541L)
                ^ ((long) pos.getZ() * 42317861L);

        return new Random(mixedSeed).nextDouble();
    }

    private boolean shouldReplace(BlockState state) {
        return state.is(Blocks.STONE)
                || state.is(Blocks.DEEPSLATE)
                || state.is(Blocks.TUFF)
                || state.is(Blocks.ANDESITE)
                || state.is(Blocks.DIORITE)
                || state.is(Blocks.GRANITE);
    }
    private Optional<Block> getOreBlockForY(OreDefinition ore, int y) {
        String blockId = ore.sourceOreBlock();

        if (y <= ore.deepslateBelowY() && !ore.deepslateOreBlock().isBlank()) {
            blockId = ore.deepslateOreBlock();
        }

        ResourceLocation resourceLocation = ResourceLocation.parse(blockId);

        return BuiltInRegistries.BLOCK.getOptional(resourceLocation);
    }
}