package com.grim3212.assorted.pokeball;

import com.grim3212.assorted.pokeball.client.PokeballClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedPokeballFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PokeballClient.init();
    }
}
