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
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBucketsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedBucketsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);
        modBus.addListener(this::registerCapabilities);

        BucketsCommonMod.init();
    }

    /** Registers each better bucket's {@code ResourceHandler<FluidResource>} item capability. */
    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        for (Item item : BucketsItems.buckets()) {
            if (item instanceof BetterBucketItem bucket) {
                event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new NeoForgeBetterBucketFluidHandler(access, bucket, bucket.getBreakStack(), bucket.getEmptyStack(), bucket.getMaximumMillibuckets()), item);
            }
        }
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new BucketsRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new BucketsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new BucketsItemModelProvider(packOutput));
        event.addProvider(new BucketsLanguageProvider(packOutput));
        event.addProvider(new BucketsManualProvider(packOutput));
    }
}
