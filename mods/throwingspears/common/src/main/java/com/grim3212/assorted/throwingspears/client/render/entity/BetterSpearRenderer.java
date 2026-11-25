package com.grim3212.assorted.throwingspears.client.render.entity;

import com.grim3212.assorted.throwingspears.client.render.model.SpearModel;
import com.grim3212.assorted.throwingspears.client.render.model.ThrowingSpearsModelLayers;
import com.grim3212.assorted.throwingspears.common.entity.BetterSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** A thrown spear, drawn as vanilla draws a thrown trident, with the texture of the spear's material. */
public class BetterSpearRenderer extends EntityRenderer<BetterSpearEntity> {

    private final SpearModel spearModel;

    public BetterSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.spearModel = new SpearModel(context.bakeLayer(ThrowingSpearsModelLayers.SPEAR));
    }

    @Override
    public void render(BetterSpearEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffers, this.spearModel.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
        this.spearModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BetterSpearEntity entity) {
        return this.spearModel.getTexture(entity.getSpearStack().getItem());
    }
}
