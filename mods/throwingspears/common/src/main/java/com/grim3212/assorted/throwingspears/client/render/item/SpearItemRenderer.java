package com.grim3212.assorted.throwingspears.client.render.item;

import com.grim3212.assorted.throwingspears.client.render.model.SpearModel;
import com.grim3212.assorted.throwingspears.client.render.model.ThrowingSpearsModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Draws a throwing spear as vanilla draws a trident: the flat icon in the GUI, on the ground and in item frames, and
 * the spear model everywhere else. Vanilla only swaps models for its own trident, so the icon is a standalone model
 * that the loader is told to bake, and this renderer draws it in place of the {@code builtin/entity} model.
 */
public class SpearItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ResourceLocation texture;
    private final ResourceLocation iconModel;
    private final Function<ResourceLocation, BakedModel> standaloneModels;
    private SpearModel model;
    private BakedModel icon;

    /**
     * @param texture          the spear's entity texture, which the model is drawn with
     * @param iconModel        the flat icon's model json, which the loader bakes as a standalone model
     * @param standaloneModels the loader's lookup for a baked standalone model
     */
    public SpearItemRenderer(ResourceLocation texture, ResourceLocation iconModel, Function<ResourceLocation, BakedModel> standaloneModels) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.texture = texture;
        this.iconModel = iconModel;
        this.standaloneModels = standaloneModels;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        this.model = null;
        this.icon = null;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED) {
            if (this.icon == null) {
                this.icon = this.standaloneModels.apply(this.iconModel);
            }

            // Undo the in-hand model's transform that ItemRenderer applied before calling here, then draw the icon
            // with its own; the push keeps the stack balanced for the pop ItemRenderer does afterwards.
            poseStack.popPose();
            poseStack.pushPose();
            Minecraft.getInstance().getItemRenderer().render(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, this.icon);
            return;
        }

        if (this.model == null) {
            this.model = new SpearModel(Minecraft.getInstance().getEntityModels().bakeLayer(ThrowingSpearsModelLayers.SPEAR));
        }

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.texture), false, stack.hasFoil());
        this.model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
