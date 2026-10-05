package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.registry.IRegistryObject;

/** A material's bucket and the milk bucket that shares its texture and capacity. */
public record BucketPair(ToolTier tier, IRegistryObject<BetterBucketItem> bucket, IRegistryObject<BetterMilkBucketItem> milk) {
}
