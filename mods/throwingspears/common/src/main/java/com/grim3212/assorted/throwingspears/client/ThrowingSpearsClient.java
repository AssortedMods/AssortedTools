package com.grim3212.assorted.throwingspears.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.throwingspears.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.throwingspears.client.render.item.SpearSpecialRenderer;
import com.grim3212.assorted.throwingspears.client.render.model.SpearModel;
import com.grim3212.assorted.throwingspears.client.render.model.ThrowingSpearsModelLayers;
import com.grim3212.assorted.throwingspears.common.entity.ThrowingSpearsEntities;

public class ThrowingSpearsClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityLayer(ThrowingSpearsModelLayers.SPEAR, SpearModel::createLayer);
        // The item model json picks the renderer by id, so code only registers the id and its codec.
        ClientServices.CLIENT.registerSpecialModelRenderers(register -> register.registerSpecialModelRenderer(SpearSpecialRenderer.ID, SpearSpecialRenderer.Unbaked.MAP_CODEC));
        ClientServices.CLIENT.registerEntityRenderer(() -> ThrowingSpearsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);
    }
}
