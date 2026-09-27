package com.grim3212.assorted.pokeball.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.pokeball.common.entity.PokeballEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class PokeballClient {

    public static void init() {
        ClientServices.CLIENT.registerEntityRenderer(() -> PokeballEntities.POKEBALL.get(), ThrownItemRenderer::new);
    }
}
