package com.grim3212.assorted.buckets.client;

import com.grim3212.assorted.buckets.client.color.FluidContainerTintSource;
import com.grim3212.assorted.buckets.client.model.fluidcontainer.FluidContainerItemModel;
import com.grim3212.assorted.lib.platform.ClientServices;

public class BucketsClient {

    public static void init() {
        // FluidContainerItemModel names this source on its fluid layer itself; registering it lets a resource pack name it too.
        ClientServices.CLIENT.registerItemTintSource(FluidContainerTintSource.ID, FluidContainerTintSource.MAP_CODEC);
        // Not a model json loader: the fluid it draws is only known after baking, and a loader has to finish its quads during it.
        ClientServices.CLIENT.registerItemModelType(FluidContainerItemModel.ID, FluidContainerItemModel.Unbaked.MAP_CODEC);
    }
}
