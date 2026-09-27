package com.grim3212.assorted.buckets.client.data;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.client.model.fluidcontainer.FluidContainerItemModel;
import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.LibConstants;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.stream.Stream;

/** The textures live in {@code item/buckets/} beside the fluid mask and covers, so models name them explicitly. */
public class BucketsItemModelProvider extends ModelProvider {

    // TextureSlot has no equals, so each extra slot FluidContainerItemModel reads is made once and shared.
    private static final TextureSlot BASE = TextureSlot.create("base");
    private static final TextureSlot FLUID = TextureSlot.create("fluid");
    private static final TextureSlot COVER = TextureSlot.create("cover");

    private static final ModelTemplate BUCKET_BODY = ExtendedModelTemplateBuilder.builder()
            .parent(Identifier.fromNamespaceAndPath(LibConstants.MOD_ID, "item/default"))
            .requiredTextureSlot(TextureSlot.PARTICLE)
            .requiredTextureSlot(BASE)
            .requiredTextureSlot(FLUID)
            .requiredTextureSlot(COVER)
            .build();

    public BucketsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Buckets item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (BucketPair pair : BucketsItems.pairs()) {
            bucket(itemModels, pair.bucket().get(), pair.milk().get());
        }
    }

    /** A fluid container model, which draws whatever fluid the stack holds, and the milk bucket's plain two layer model over the same texture. */
    private void bucket(ItemModelGenerators itemModels, Item bucket, Item milkBucket) {
        String name = name(bucket);
        Material bucketTexture = prefixed("item/buckets/" + name);

        Identifier bodyModel = BUCKET_BODY.create(modelId(name), new TextureMapping()
                .put(TextureSlot.PARTICLE, bucketTexture)
                .put(BASE, bucketTexture)
                .put(FLUID, prefixed("item/buckets/bucket_fluid"))
                .put(COVER, prefixed("item/buckets/bucket_covered")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(bucket, new FluidContainerItemModel.Unbaked(bodyModel, true, true, true));

        Identifier milkModel = ModelTemplates.TWO_LAYERED_ITEM.create(modelId(name(milkBucket)),
                TextureMapping.layered(bucketTexture, prefixed("item/buckets/overlay_milk")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(milkBucket, ItemModelUtils.plainModel(milkModel));
    }

    private static String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private static Identifier modelId(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path);
    }

    private static Material prefixed(String texture) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, texture));
    }
}
