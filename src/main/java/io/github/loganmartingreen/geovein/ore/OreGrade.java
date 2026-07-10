package io.github.loganmartingreen.geovein.ore;

public enum OreGrade {
    POOR("Poor", 0.25f),
    COMMON("Common", 1.0f),
    RICH("Rich", 1.5f),
    NATIVE("Native", 3.0f);

    private final String displayName;
    private final float yieldMultiplier;

    OreGrade(String displayName, float yieldMultiplier) {
        this.displayName = displayName;
        this.yieldMultiplier = yieldMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getYieldMultiplier() {
        return yieldMultiplier;
    }
}