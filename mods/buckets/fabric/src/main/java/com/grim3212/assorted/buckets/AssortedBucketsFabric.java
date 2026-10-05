package com.grim3212.assorted.buckets;

import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.buckets.common.item.FabricBetterBucketFluidHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.world.item.Item;

public class AssortedBucketsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BucketsCommonMod.init();
        FluidStorage.ITEM.registerForItems((i, c) -> new FabricBetterBucketFluidHandler(c), BucketsItems.buckets().toArray(new Item[0]));
    }
}
