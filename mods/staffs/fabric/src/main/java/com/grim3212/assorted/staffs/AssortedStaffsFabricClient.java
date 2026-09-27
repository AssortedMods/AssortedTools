package com.grim3212.assorted.staffs;

import com.grim3212.assorted.staffs.client.FabricFrozenClient;
import com.grim3212.assorted.staffs.client.StaffsClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedStaffsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        StaffsClient.init();
        FabricFrozenClient.init();
    }
}
