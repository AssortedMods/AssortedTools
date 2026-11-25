package com.grim3212.assorted.buckets;

import com.grim3212.assorted.buckets.client.data.BucketsItemModelProvider;
import com.grim3212.assorted.buckets.client.data.BucketsLanguageProvider;
import com.grim3212.assorted.buckets.client.data.BucketsManualProvider;
import com.grim3212.assorted.buckets.common.item.BetterBucketItem;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.buckets.common.item.NeoForgeBetterBucketFluidHandler;
import com.grim3212.assorted.buckets.data.BucketsItemTagProvider;
import com.grim3212.assorted.buckets.data.BucketsRecipes;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBucketsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedBucketsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        BucketsCommonMod.init();
    }

    /** Registers each better bucket's fluid handler item capability. */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        for (Item item : BucketsItems.buckets()) {
            if (item instanceof BetterBucketItem bucket) {
                event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new NeoForgeBetterBucketFluidHandler(stack, bucket, bucket.getBreakStack(), bucket.getEmptyStack(), bucket.getMaximumMillibuckets()), item);
            }
        }
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(BucketsRecipes::new).addTo(event);
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, fileHelper, new BucketsItemTagProvider(packOutput, lookupProvider, noBlockTags)));

        generator.addProvider(event.includeClient(), new BucketsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new BucketsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new BucketsManualProvider(packOutput));
    }
}
