package com.grim3212.assorted.throwingspears.client;

import com.grim3212.assorted.throwingspears.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;

/** The client-only entry point: a second {@code @Mod} for the same mod id, constructed only on the client. */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class AssortedThrowingSpearsNeoForgeClient {

    public AssortedThrowingSpearsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        // The spears' flat icons are no item's own model, so NeoForge is asked to bake them standalone.
        modBus.addListener((ModelEvent.RegisterAdditional event) -> ThrowingSpearsClient.iconModels().forEach(id -> event.register(ModelResourceLocation.standalone(id))));
        ThrowingSpearsClient.init(id -> Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(id)));
    }
}
