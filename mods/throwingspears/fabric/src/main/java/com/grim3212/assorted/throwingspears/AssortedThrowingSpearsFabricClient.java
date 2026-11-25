package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.throwingspears.client.ThrowingSpearsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;

public class AssortedThrowingSpearsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // The spears' flat icons are no item's own model, so Fabric is asked to bake them as extra models.
        ModelLoadingPlugin.register(context -> context.addModels(ThrowingSpearsClient.iconModels()));
        ThrowingSpearsClient.init(id -> Minecraft.getInstance().getModelManager().getModel(id));
    }
}
