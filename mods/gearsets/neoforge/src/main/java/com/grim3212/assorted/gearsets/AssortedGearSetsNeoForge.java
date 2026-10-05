package com.grim3212.assorted.gearsets;

import com.grim3212.assorted.gearsets.client.data.GearSetsEquipmentAssetProvider;
import com.grim3212.assorted.gearsets.client.data.GearSetsItemModelProvider;
import com.grim3212.assorted.gearsets.client.data.GearSetsLanguageProvider;
import com.grim3212.assorted.gearsets.client.data.GearSetsManualProvider;
import com.grim3212.assorted.gearsets.data.GearSetsItemTagProvider;
import com.grim3212.assorted.gearsets.data.GearSetsRecipes;
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
public class AssortedGearSetsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedGearSetsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        GearSetsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new GearSetsRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new GearSetsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
    }

    /** Client datagen: models, equipment assets, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new GearSetsItemModelProvider(packOutput));
        event.addProvider(new GearSetsEquipmentAssetProvider(packOutput));
        event.addProvider(new GearSetsLanguageProvider(packOutput));
        event.addProvider(new GearSetsManualProvider(packOutput));
    }
}
