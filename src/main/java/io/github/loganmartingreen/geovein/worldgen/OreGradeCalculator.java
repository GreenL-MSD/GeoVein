package io.github.loganmartingreen.geovein.worldgen;

import io.github.loganmartingreen.geovein.ore.OreGrade;
import net.minecraft.core.BlockPos;

import java.util.Random;

public class OreGradeCalculator {
    public static OreGrade calculateGrade(Deposit deposit, BlockPos pos) {
        double distance = deposit.getNormalizedDistanceFromCore(pos);

        double noise = getSmallDeterministicNoise(deposit.seed(), pos);
        double richness = 1.0 - distance + noise;

        if (richness >= 0.82) {
            return OreGrade.NATIVE;
        }

        if (richness >= 0.62) {
            return OreGrade.RICH;
        }

        if (richness >= 0.35) {
            return OreGrade.COMMON;
        }

        return OreGrade.POOR;
    }

    private static double getSmallDeterministicNoise(long seed, BlockPos pos) {
        long mixedSeed = seed
                ^ ((long) pos.getX() * 341873128712L)
                ^ ((long) pos.getY() * 132897987541L)
                ^ ((long) pos.getZ() * 42317861L);

        Random random = new Random(mixedSeed);

        return (random.nextDouble() - 0.5) * 0.18;
    }
}