package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;

import java.util.function.Supplier;

/** How much one material's better bucket holds and what it survives. Baked in at registration, so changes need a restart. */
public class BucketConfig {

    public final Supplier<Integer> maxBuckets;
    public final Supplier<Integer> milkingLevel;
    public final Supplier<Double> maxPickupTemp;
    public final Supplier<Boolean> breaksAfterUse;

    public BucketConfig(IConfigurationBuilder builder, String path, String name, int maxBuckets, int milkingLevel, float maxPickupTemp, boolean breaksAfterUse) {
        String prefix = path + "." + name + ".";
        this.maxBuckets = builder.defineInteger(prefix + "maxBuckets", maxBuckets, 1, 1000, "The maximum number of buckets that this materials bucket can hold.");
        this.milkingLevel = builder.defineInteger(prefix + "milkingLevel", milkingLevel, 0, 5, "The milking level that will be set for this materials bucket. By default only 0-3");
        this.maxPickupTemp = builder.defineDouble(prefix + "maxPickupTemp", maxPickupTemp, 0F, 1000000F, "The maximum temp of a fluid this materials bucket can pickup.");
        this.breaksAfterUse = builder.defineBoolean(prefix + "breaksAfterUse", breaksAfterUse, "Is this material so weak that the bucket will break after placing a fluid.");
    }

    public int getMaxBuckets() {
        return this.maxBuckets.get();
    }

    public int getMilkingLevel() {
        return this.milkingLevel.get();
    }

    public float getMaxPickupTemp() {
        return this.maxPickupTemp.get().floatValue();
    }

    public boolean getBreaksAfterUse() {
        return this.breaksAfterUse.get();
    }
}
