package com.grim3212.assorted.suits;

import com.grim3212.assorted.lib.data.ForgeDatapackRegistryProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.suits.client.data.SuitsEquipmentAssetProvider;
import com.grim3212.assorted.suits.client.data.SuitsItemModelProvider;
import com.grim3212.assorted.suits.client.data.SuitsLanguageProvider;
import com.grim3212.assorted.suits.client.data.SuitsManualProvider;
import com.grim3212.assorted.suits.data.SuitsEnchantmentData;
import com.grim3212.assorted.suits.data.SuitsEnchantmentTagProvider;
import com.grim3212.assorted.suits.data.SuitsItemTagProvider;
import com.grim3212.assorted.suits.data.SuitsRecipes;
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
public class AssortedSuitsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedSuitsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        SuitsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new SuitsRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new SuitsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
        // Enchantments are datapack registry content, not registered from code.
        event.addProvider(new ForgeDatapackRegistryProvider(Constants.MOD_ID, new SuitsEnchantmentData()).datpackEntriesProvider(packOutput, lookupProvider));
        event.addProvider(new SuitsEnchantmentTagProvider(packOutput, lookupProvider));
    }

    /** Client datagen: models, equipment assets, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new SuitsItemModelProvider(packOutput));
        event.addProvider(new SuitsEquipmentAssetProvider(packOutput));
        event.addProvider(new SuitsLanguageProvider(packOutput));
        event.addProvider(new SuitsManualProvider(packOutput));
    }
}
