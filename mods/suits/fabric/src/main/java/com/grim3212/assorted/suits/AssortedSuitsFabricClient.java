package com.grim3212.assorted.suits;

import com.grim3212.assorted.suits.client.SuitsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedSuitsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SuitsClient.init();
    }
}
