package com.grim3212.assorted.boomerangs.client.render.entity;

import com.grim3212.assorted.boomerangs.common.entity.BoomerangEntity;
import com.grim3212.assorted.boomerangs.common.entity.BoomerangsEntities;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Draws a boomerang as its item, turned to its heading and spun by its synced rotation. */
public class BoomerangRenderer extends EntityRenderer<BoomerangEntity> {

    private final ItemRenderer itemRenderer;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public void render(BoomerangEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot + 90.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(entity.getBoomerangRotation()));
        this.itemRenderer.renderStatic(getItemStackForRender(entity), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffers, entity.level(), entity.getId());
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    /** The item is drawn from the block atlas, as vanilla's thrown items are. */
    @Override
    public ResourceLocation getTextureLocation(BoomerangEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    private ItemStack getItemStackForRender(BoomerangEntity entityIn) {
        if (entityIn.getType() == BoomerangsEntities.DIAMOND_BOOMERANG.get()) {
            return new ItemStack(BoomerangsItems.DIAMOND_BOOMERANG.get());
        }
        return new ItemStack(BoomerangsItems.WOOD_BOOMERANG.get());
    }
}
