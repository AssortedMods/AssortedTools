package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class BucketsDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<DataComponentType<BucketContents>> BUCKET_CONTENTS = DATA_COMPONENTS.register("bucket_contents",
            () -> new DataComponentType.Builder<BucketContents>().persistent(BucketContents.CODEC).networkSynchronized(BucketContents.STREAM_CODEC).build());

    // Runs before BucketsItems, whose buckets carry this as a default component.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(BUCKET_CONTENTS);
    }
}
