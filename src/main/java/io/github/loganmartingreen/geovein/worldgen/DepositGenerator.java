package io.github.loganmartingreen.geovein.worldgen;

import io.github.loganmartingreen.geovein.data.OreDefinition;
import net.minecraft.core.BlockPos;

import java.util.Optional;
import java.util.Random;

public class DepositGenerator {
    public static Optional<Deposit> generateForRegion(
            OreDefinition ore,
            long worldSeed,
            int regionX,
            int regionZ
    ) {
        long seed = mixSeed(worldSeed, ore.id(), regionX, regionZ);
        Random random = new Random(seed);

        if (random.nextDouble() > ore.spawnChancePerRegion()) {
            return Optional.empty();
        }

        int regionSizeBlocks = ore.regionSizeChunks() * 16;

        int minX = regionX * regionSizeBlocks;
        int minZ = regionZ * regionSizeBlocks;

        int centerX = minX + random.nextInt(regionSizeBlocks);
        int centerZ = minZ + random.nextInt(regionSizeBlocks);
        int centerY = randomBetween(random, ore.minY(), ore.maxY());

        Deposit deposit = new Deposit(
                ore,
                new BlockPos(centerX, centerY, centerZ),
                ore.length(),
                ore.width(),
                ore.height(),
                seed
        );

        return Optional.of(deposit);
    }

    private static int randomBetween(Random random, int min, int max) {
        if (max <= min) {
            return min;
        }

        return min + random.nextInt(max - min + 1);
    }

    private static long mixSeed(long worldSeed, String oreId, int regionX, int regionZ) {
        long seed = worldSeed;

        seed ^= oreId.hashCode() * 31L;
        seed ^= (long) regionX * 341873128712L;
        seed ^= (long) regionZ * 132897987541L;

        return seed;
    }
}