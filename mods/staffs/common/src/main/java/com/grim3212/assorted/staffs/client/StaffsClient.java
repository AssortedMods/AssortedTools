package com.grim3212.assorted.staffs.client;

import com.grim3212.assorted.lib.client.key.ModeSwitchKey;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.staffs.common.entity.StaffsEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StaffsClient {

    public static void init() {
        ModeSwitchKey.enable();
        ClientServices.CLIENT.registerEntityRenderer(() -> StaffsEntities.ICE_CHARGE.get(), ThrownItemRenderer::new);
    }
}
