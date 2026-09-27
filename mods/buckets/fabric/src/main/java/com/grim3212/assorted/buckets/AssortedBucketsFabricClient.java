package com.grim3212.assorted.buckets;

import com.grim3212.assorted.buckets.client.BucketsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedBucketsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BucketsClient.init();
    }
}
