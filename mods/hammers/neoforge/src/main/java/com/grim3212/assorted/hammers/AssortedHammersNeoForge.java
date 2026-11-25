package com.grim3212.assorted.hammers;

import com.grim3212.assorted.hammers.client.data.HammersItemModelProvider;
import com.grim3212.assorted.hammers.client.data.HammersLanguageProvider;
import com.grim3212.assorted.hammers.client.data.HammersManualProvider;
import com.grim3212.assorted.hammers.data.HammersItemTagProvider;
import com.grim3212.assorted.hammers.data.HammersRecipes;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedHammersNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedHammersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        HammersCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(HammersRecipes::new).addTo(event);
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, fileHelper, new HammersItemTagProvider(packOutput, lookupProvider, noBlockTags)));

        generator.addProvider(event.includeClient(), new HammersItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new HammersLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new HammersManualProvider(packOutput));
    }
}
