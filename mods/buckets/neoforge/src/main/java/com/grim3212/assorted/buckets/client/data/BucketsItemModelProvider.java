package com.grim3212.assorted.buckets.client.data;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.LibConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** The textures live in {@code item/buckets/} beside the fluid mask and covers, so models name them explicitly. */
public class BucketsItemModelProvider extends ItemModelProvider {

    public BucketsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Buckets item models";
    }

    @Override
    protected void registerModels() {
        for (BucketPair pair : BucketsItems.pairs()) {
            bucket(pair.bucket().get(), pair.milk().get());
        }
    }

    /** A fluid container model, which draws whatever fluid the stack holds, and the milk bucket's plain two layer model over the same texture. */
    private void bucket(Item bucket, Item milkBucket) {
        String name = name(bucket);
        ResourceLocation bucketTexture = this.modLoc("item/buckets/" + name);

        // The loader replaces the geometry outright; the parent supplies the display transforms and gui light.
        this.getBuilder(name).parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(LibConstants.MOD_ID, "item/default")))
                .texture("particle", bucketTexture)
                .texture("base", bucketTexture)
                .texture("fluid", this.modLoc("item/buckets/bucket_fluid"))
                .texture("cover", this.modLoc("item/buckets/bucket_covered"))
                .customLoader(FluidContainerModelBuilder::begin).flipGas(true).end();

        this.getBuilder(name(milkBucket)).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", bucketTexture)
                .texture("layer1", this.modLoc("item/buckets/overlay_milk"));
    }

    private static String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
