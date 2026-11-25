package com.grim3212.assorted.staffs.client;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.client.render.FrozenLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * The client-only entry point: a second {@code @Mod} for the same mod id, constructed only on the client.
 * It draws frozen mobs as ice, giving every living entity's renderer a {@link FrozenLayer}.
 */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class AssortedStaffsNeoForgeClient {

    public AssortedStaffsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        StaffsClient.init();

        modBus.addListener(AssortedStaffsNeoForgeClient::addLayers);
    }

    // The renderers come back untyped, so the layer is built raw; it only draws for mobs a staff froze.
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            if (event.getRenderer(type) instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(new FrozenLayer<>(renderer));
            }
        }
    }
}
