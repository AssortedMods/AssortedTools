package com.grim3212.assorted.extramaterials;

import com.grim3212.assorted.extramaterials.client.data.ExtraMaterialsEquipmentAssetProvider;
import com.grim3212.assorted.extramaterials.client.data.ExtraMaterialsItemModelProvider;
import com.grim3212.assorted.extramaterials.client.data.ExtraMaterialsLanguageProvider;
import com.grim3212.assorted.extramaterials.client.data.ExtraMaterialsManualProvider;
import com.grim3212.assorted.extramaterials.data.ExtraMaterialsItemTagProvider;
import com.grim3212.assorted.extramaterials.data.ExtraMaterialsRecipes;
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
public class AssortedExtraMaterialsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedExtraMaterialsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        ExtraMaterialsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ExtraMaterialsRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new ExtraMaterialsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
    }

    /** Client datagen: models, equipment assets, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new ExtraMaterialsItemModelProvider(packOutput));
        event.addProvider(new ExtraMaterialsEquipmentAssetProvider(packOutput));
        event.addProvider(new ExtraMaterialsLanguageProvider(packOutput));
        event.addProvider(new ExtraMaterialsManualProvider(packOutput));
    }
}
