package com.grim3212.assorted.hammers;

import com.grim3212.assorted.hammers.client.data.HammersItemModelProvider;
import com.grim3212.assorted.hammers.client.data.HammersLanguageProvider;
import com.grim3212.assorted.hammers.client.data.HammersManualProvider;
import com.grim3212.assorted.hammers.data.HammersItemTagProvider;
import com.grim3212.assorted.hammers.data.HammersRecipes;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedHammersNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedHammersNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        HammersCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new HammersRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new HammersItemTagProvider(packOutput, lookupProvider, noBlockTags)));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new HammersItemModelProvider(packOutput));
        event.addProvider(new HammersLanguageProvider(packOutput));
        event.addProvider(new HammersManualProvider(packOutput));
    }
}
