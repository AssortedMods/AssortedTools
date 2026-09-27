package com.grim3212.assorted.boomerangs;

import com.grim3212.assorted.boomerangs.client.BoomerangsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedBoomerangsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BoomerangsClient.init();
    }
}
