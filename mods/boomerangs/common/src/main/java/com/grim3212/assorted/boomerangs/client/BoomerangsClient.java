package com.grim3212.assorted.boomerangs.client;

import com.grim3212.assorted.boomerangs.client.render.entity.BoomerangRenderer;
import com.grim3212.assorted.boomerangs.common.entity.BoomerangsEntities;
import com.grim3212.assorted.lib.platform.ClientServices;

public class BoomerangsClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityRenderer(() -> BoomerangsEntities.WOOD_BOOMERANG.get(), BoomerangRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(() -> BoomerangsEntities.DIAMOND_BOOMERANG.get(), BoomerangRenderer::new);
    }
}
