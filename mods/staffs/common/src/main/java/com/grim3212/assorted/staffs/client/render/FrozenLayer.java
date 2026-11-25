package com.grim3212.assorted.staffs.client.render;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * Draws a frozen mob's own model again in an opaque ice texture, covering it the way vanilla's eye
 * layers do. Whether a mob is frozen is read off the entity, which each loader's attachment syncs; see
 * {@code IFrozenStorage}.
 */
public class FrozenLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/frozen.png");

    public FrozenLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (FrozenMobs.isFrozen(entity)) {
            // The parent model is already posed for this frame, so it is drawn again as it stands.
            renderColoredCutoutModel(this.getParentModel(), TEXTURE, poseStack, buffers, packedLight, entity, -1);
        }
    }
}
