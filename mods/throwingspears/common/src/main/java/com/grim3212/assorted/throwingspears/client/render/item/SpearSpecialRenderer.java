package com.grim3212.assorted.throwingspears.client.render.item;

import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.client.render.model.SpearModel;
import com.grim3212.assorted.throwingspears.client.render.model.ThrowingSpearsModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Draws a spear held in hand, mirroring vanilla's {@code TridentSpecialRenderer}. The item model
 * json handles the GUI/ground/fixed model, the throwing-model swap and scale, not this class.
 */
public class SpearSpecialRenderer implements NoDataSpecialModelRenderer {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spear");

    private final SpearModel model;
    private final Identifier texture;

    public SpearSpecialRenderer(SpearModel model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, this.texture, lightCoords, overlayCoords, outlineColor, null);
        if (hasFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, outlineColor, null);
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    /**
     * The material's texture is the only thing that varies between spears, so it's a field of the
     * unbaked renderer; the renderer never sees the {@code ItemStack} beyond {@code extractArgument}.
     */
    public record Unbaked(Identifier texture) implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<SpearSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        Identifier.CODEC.fieldOf("texture").forGetter(SpearSpecialRenderer.Unbaked::texture)
                ).apply(i, SpearSpecialRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<SpearSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpearSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new SpearSpecialRenderer(new SpearModel(context.entityModelSet().bakeLayer(ThrowingSpearsModelLayers.SPEAR)), this.texture);
        }
    }
}
