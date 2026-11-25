package com.grim3212.assorted.buckets.client;

import com.grim3212.assorted.buckets.client.model.fluidcontainer.FluidContainerModel;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;

public class BucketsClient {

    /** No tint: white, fully opaque, as 1.21.1 reads the alpha off an item colour too. */
    private static final int NO_TINT = 0xFFFFFFFF;

    public static void init() {
        ClientServices.CLIENT.registerModelLoader(FluidContainerModel.LOADER_NAME, FluidContainerModel.Loader.INSTANCE);
        // The fluid layer takes the colour of the fluid the bucket holds; the bucket's own layers are drawn as they are.
        ClientServices.CLIENT.registerItemColor((stack, tintIndex) -> tintIndex == FluidContainerModel.FLUID_TINT_INDEX
                ? Services.FLUIDS.get(stack).map(ClientServices.FLUIDS::getFluidColor).orElse(NO_TINT)
                : NO_TINT, BucketsItems::buckets);
    }
}
