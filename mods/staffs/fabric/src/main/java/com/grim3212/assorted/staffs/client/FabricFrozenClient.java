package com.grim3212.assorted.staffs.client;

import com.grim3212.assorted.staffs.client.render.FrozenLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

/** Draws frozen mobs as ice, giving every living entity's renderer a {@link FrozenLayer}. */
public final class FabricFrozenClient {

    private FabricFrozenClient() {
    }

    // The renderer comes back as LivingEntityRenderer<?, ?>, so the layer is built raw; it only draws for mobs a staff froze.
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void init() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) -> helper.register(new FrozenLayer(renderer)));
    }
}
