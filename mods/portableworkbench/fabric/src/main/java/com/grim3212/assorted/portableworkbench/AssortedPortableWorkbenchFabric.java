package com.grim3212.assorted.portableworkbench;

import net.fabricmc.api.ModInitializer;

public class AssortedPortableWorkbenchFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PortableWorkbenchCommonMod.init();
    }
}
