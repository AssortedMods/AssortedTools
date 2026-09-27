package com.grim3212.assorted.buckets.config;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.platform.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BucketsConfig {

    public final Supplier<Boolean> allowPartialBucketAmounts;
    /** Keyed by tier name. */
    public final Map<String, BucketConfig> buckets = new HashMap<>();

    public BucketsConfig() {
        // Needed at registration: a bucket bakes its capacity into its default component as it is constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        allowPartialBucketAmounts = builder.defineBoolean("better_buckets.allowPartialBucketAmounts", false, "Set to true if you would like the better buckets to be able to accept partial bucket amounts. Meaning some can get left over after placing all the full buckets.");

        bucket(builder, "wood", 1, 0, 1000f, true);
        bucket(builder, "stone", 1, 0, 5000f, true);
        bucket(builder, "gold", 4, 0, 5000f, false);
        bucket(builder, "diamond", 16, 1, 5000f, false);
        bucket(builder, "netherite", 64, 2, 10000f, false);

        bucket(builder, "tin", 4, 0);
        bucket(builder, "copper", 8, 0);
        bucket(builder, "silver", 12, 1);
        bucket(builder, "aluminum", 2, 0);
        bucket(builder, "nickel", 8, 1);
        bucket(builder, "platinum", 24, 2);
        bucket(builder, "lead", 4, 0);
        bucket(builder, "bronze", 8, 1);
        bucket(builder, "electrum", 18, 1);
        bucket(builder, "invar", 10, 1);
        bucket(builder, "steel", 16, 1);
        bucket(builder, "ruby", 8, 1);
        bucket(builder, "amethyst", 8, 0);
        bucket(builder, "sapphire", 8, 0);
        bucket(builder, "topaz", 8, 0);
        bucket(builder, "emerald", 10, 1);
        bucket(builder, "peridot", 8, 0);

        builder.setup();
    }

    public BucketConfig bucket(ToolTier tier) {
        return this.buckets.get(tier.getName());
    }

    private void bucket(IConfigurationBuilder builder, String name, int maxBuckets, int milkingLevel) {
        bucket(builder, name, maxBuckets, milkingLevel, 5000f, false);
    }

    private void bucket(IConfigurationBuilder builder, String name, int maxBuckets, int milkingLevel, float maxPickupTemp, boolean breaksAfterUse) {
        this.buckets.put(name, new BucketConfig(builder, "better_buckets", name, maxBuckets, milkingLevel, maxPickupTemp, breaksAfterUse));
    }
}
