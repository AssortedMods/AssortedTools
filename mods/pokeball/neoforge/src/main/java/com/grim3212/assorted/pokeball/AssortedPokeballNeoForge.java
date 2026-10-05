package com.grim3212.assorted.pokeball;

import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.pokeball.client.data.PokeballItemModelProvider;
import com.grim3212.assorted.pokeball.client.data.PokeballLanguageProvider;
import com.grim3212.assorted.pokeball.client.data.PokeballManualProvider;
import com.grim3212.assorted.pokeball.data.PokeballDataComponentTagProvider;
import com.grim3212.assorted.pokeball.data.PokeballItemTagProvider;
import com.grim3212.assorted.pokeball.data.PokeballRecipes;
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
public class AssortedPokeballNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedPokeballNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        PokeballCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new PokeballRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new PokeballItemTagProvider(packOutput, lookupProvider, noBlockTags)));
        event.addProvider(new PokeballDataComponentTagProvider(packOutput, lookupProvider));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new PokeballItemModelProvider(packOutput));
        event.addProvider(new PokeballLanguageProvider(packOutput));
        event.addProvider(new PokeballManualProvider(packOutput));
    }
}
