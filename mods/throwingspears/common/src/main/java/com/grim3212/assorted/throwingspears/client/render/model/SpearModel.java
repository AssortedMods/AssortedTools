package com.grim3212.assorted.throwingspears.client.render.model;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.common.item.BetterSpearItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/** The spear model. It has no animated parts, like vanilla's {@code TridentModel}. */
public class SpearModel extends Model {

    private final ModelPart root;

    public SpearModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 2F, -0.5F, 1.0F, 25.0F, 1.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("left_point", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, -5F, -0.5F, 1.0F, 7.0F, 1.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("middle_point", CubeListBuilder.create().texOffs(4, 25).addBox(-1.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("right_point", CubeListBuilder.create().texOffs(8, 25).addBox(0.5F, -4F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, consumer, packedLight, packedOverlay, color);
    }

    public static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/projectiles/wood_throwing_spear.png");

    protected final Map<ResourceLocation, ResourceLocation> cache = new HashMap<ResourceLocation, ResourceLocation>();

    public ResourceLocation getTexture(Item item) {
        ResourceLocation key = key(item);
        if (!this.cache.containsKey(key)) {
            if (item instanceof BetterSpearItem spear) {
                this.cache.put(key, texture(spear));
            } else {
                Constants.LOG.error("Tried to get spear texture for non-spear item");
                return DEFAULT_TEXTURE;
            }
        }

        return this.cache.get(key);
    }

    /** The entity texture of a spear, by the name of its material. */
    public static ResourceLocation texture(BetterSpearItem spear) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/projectiles/" + spear.getToolTier().getName() + "_throwing_spear.png");
    }

    private ResourceLocation key(Item item) {
        return Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(item);
    }
}
