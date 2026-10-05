package com.grim3212.assorted.staffs.mixin.client;

import com.grim3212.assorted.staffs.client.FabricFrozenClient;
import com.grim3212.assorted.staffs.client.render.FrozenLayer;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric API has no hook for adding data to an entity's render state during extraction, so this
 * mixin supplies the frozen flag {@link FrozenLayer} reads; NeoForge has {@code RegisterRenderStateModifiersEvent}.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void assortedstaffs$extractFrozen(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        state.setData(FabricFrozenClient.FROZEN, FrozenMobs.isFrozen(entity));
    }
}
