package com.grim3212.assorted.staffs.client;

import com.google.common.reflect.TypeToken;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.client.render.FrozenLayer;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

/**
 * The client-only entry point: a second {@code @Mod} for the same mod id, constructed only on the client.
 * It draws frozen mobs as ice, reading the flag off each living entity's render state.
 */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class AssortedStaffsNeoForgeClient {

    /** Filled in for every living entity as it is extracted, for {@link FrozenLayer} to read. */
    private static final ContextKey<Boolean> FROZEN = new ContextKey<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "frozen"));

    public AssortedStaffsNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        StaffsClient.init();

        modBus.addListener(AssortedStaffsNeoForgeClient::registerRenderStateModifiers);
        modBus.addListener(AssortedStaffsNeoForgeClient::addLayers);
    }

    private static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.<LivingEntity, LivingEntityRenderState>registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> state.setRenderData(FROZEN, FrozenMobs.isFrozen(entity)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            if (event.getRenderer(type) instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(new FrozenLayer<>(renderer, state -> ((LivingEntityRenderState) state).getRenderDataOrDefault(FROZEN, false)));
            }
        }
    }
}
