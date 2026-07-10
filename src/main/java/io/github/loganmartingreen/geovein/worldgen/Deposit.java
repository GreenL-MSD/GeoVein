package io.github.loganmartingreen.geovein.worldgen;

import io.github.loganmartingreen.geovein.data.OreDefinition;
import net.minecraft.core.BlockPos;

public record Deposit(
        OreDefinition ore,
        BlockPos center,
        int length,
        int width,
        int height,
        long seed
) {
    public boolean contains(BlockPos pos) {
        return switch (ore.shape()) {
            case ELLIPSOID -> isInsideEllipsoid(pos);
            case SPHERE -> isInsideSphere(pos);
            case SHEET -> isInsideSheet(pos);
            case PIPE -> isInsidePipe(pos);
            case CONE -> isInsideCone(pos);
        };
    }

    public double getNormalizedDistanceFromCore(BlockPos pos) {
        double dx = (double) (pos.getX() - center.getX()) / (length / 2.0);
        double dy = (double) (pos.getY() - center.getY()) / (height / 2.0);
        double dz = (double) (pos.getZ() - center.getZ()) / (width / 2.0);

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private boolean isInsideEllipsoid(BlockPos pos) {
        return getNormalizedDistanceFromCore(pos) <= 1.0;
    }

    private boolean isInsideSphere(BlockPos pos) {
        int radius = Math.max(length, Math.max(width, height)) / 2;

        double dx = pos.getX() - center.getX();
        double dy = pos.getY() - center.getY();
        double dz = pos.getZ() - center.getZ();

        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    private boolean isInsideSheet(BlockPos pos) {
        return Math.abs(pos.getX() - center.getX()) <= length / 2
                && Math.abs(pos.getZ() - center.getZ()) <= width / 2
                && Math.abs(pos.getY() - center.getY()) <= height / 2;
    }

    private boolean isInsidePipe(BlockPos pos) {
        double dx = (double) (pos.getX() - center.getX()) / (width / 2.0);
        double dz = (double) (pos.getZ() - center.getZ()) / (width / 2.0);

        boolean insideRadius = dx * dx + dz * dz <= 1.0;
        boolean insideHeight = Math.abs(pos.getY() - center.getY()) <= length / 2;

        return insideRadius && insideHeight;
    }

    private boolean isInsideCone(BlockPos pos) {
        double verticalProgress = (double) (pos.getY() - (center.getY() - height / 2)) / height;

        if (verticalProgress < 0.0 || verticalProgress > 1.0) {
            return false;
        }

        double radiusMultiplier = 1.0 - verticalProgress;

        double xRadius = (length / 2.0) * radiusMultiplier;
        double zRadius = (width / 2.0) * radiusMultiplier;

        if (xRadius <= 0.0 || zRadius <= 0.0) {
            return false;
        }

        double dx = (pos.getX() - center.getX()) / xRadius;
        double dz = (pos.getZ() - center.getZ()) / zRadius;

        return dx * dx + dz * dz <= 1.0;
    }
}