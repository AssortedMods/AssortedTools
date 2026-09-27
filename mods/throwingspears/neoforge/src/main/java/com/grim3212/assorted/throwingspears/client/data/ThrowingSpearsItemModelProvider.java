package com.grim3212.assorted.throwingspears.client.data;

import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.client.render.item.SpearSpecialRenderer;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.TridentSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.stream.Stream;

/** Throwing spears as vanilla draws a trident: flat in the GUI, on the ground and in frames, a model in the hand. */
public class ThrowingSpearsItemModelProvider extends ModelProvider {

    /** Only {@code head} is set; every other perspective comes from {@code minecraft:item/trident_in_hand}. */
    private static final ModelTemplate SPEAR_IN_HAND = ExtendedModelTemplateBuilder.builder()
            .parent(Identifier.withDefaultNamespace("item/trident_in_hand"))
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .transform(ItemDisplayContext.HEAD, t -> t.rotation(0.0F, 180.0F, 120.0F).translation(8.0F, 10.0F, -11.0F).scale(1.5F))
            .build();

    private static final ModelTemplate SPEAR_THROWING = ExtendedModelTemplateBuilder.builder()
            .parent(Identifier.withDefaultNamespace("item/trident_throwing"))
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .build();

    public ThrowingSpearsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Throwing Spears item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (Item spear : ThrowingSpearsItems.spears()) {
            spear(itemModels, spear);
        }
    }

    /** Modelled on vanilla's {@code generateTrident}; vanilla's {@code generateSpear} has no special renderer or throwing pose. */
    private void spear(ItemModelGenerators itemModels, Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        Material particle = new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name));
        TextureMapping particleMapping = new TextureMapping().put(TextureSlot.PARTICLE, particle);

        Identifier guiModel = ModelTemplates.FLAT_ITEM.create(modelId(name + "_gui"), TextureMapping.layer0(particle), itemModels.modelOutput);
        Identifier inHandModel = SPEAR_IN_HAND.create(modelId(name), particleMapping, itemModels.modelOutput);
        Identifier throwingModel = SPEAR_THROWING.create(modelId(name + "_throwing"), particleMapping, itemModels.modelOutput);

        // The entity texture, not the atlas sprite: SpearSpecialRenderer draws a Model.
        SpearSpecialRenderer.Unbaked renderer = new SpearSpecialRenderer.Unbaked(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/projectiles/" + name + ".png"));

        ItemModel.Unbaked flat = ItemModelUtils.plainModel(guiModel);
        ItemModel.Unbaked inHand = ItemModelUtils.conditional(
                TridentSpecialRenderer.DEFAULT_TRANSFORMATION,
                ItemModelUtils.isUsingItem(),
                ItemModelUtils.specialModel(throwingModel, renderer),
                ItemModelUtils.specialModel(inHandModel, renderer));

        itemModels.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(flat, inHand));
    }

    private static Identifier modelId(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path);
    }
}
