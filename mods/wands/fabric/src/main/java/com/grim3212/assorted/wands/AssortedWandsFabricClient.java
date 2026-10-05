package com.grim3212.assorted.wands;

import com.grim3212.assorted.wands.client.WandsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedWandsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WandsClient.init();
    }
}
