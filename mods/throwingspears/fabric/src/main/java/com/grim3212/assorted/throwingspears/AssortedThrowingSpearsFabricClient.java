package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.throwingspears.client.ThrowingSpearsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedThrowingSpearsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ThrowingSpearsClient.init();
    }
}
