package com.grim3212.assorted.suits;

import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.suits.client.data.SuitsItemModelProvider;
import com.grim3212.assorted.suits.client.data.SuitsLanguageProvider;
import com.grim3212.assorted.suits.client.data.SuitsManualProvider;
import com.grim3212.assorted.suits.data.SuitsEnchantmentData;
import com.grim3212.assorted.suits.data.SuitsEnchantmentTagProvider;
import com.grim3212.assorted.suits.data.SuitsItemTagProvider;
import com.grim3212.assorted.suits.data.SuitsRecipes;
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
public class AssortedSuitsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedSuitsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        SuitsCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).world(new SuitsEnchantmentData()).recipes(SuitsRecipes::new).addTo(event);
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, fileHelper, new SuitsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
        // Enchantments are datapack registry content, not registered from code.
        generator.addProvider(event.includeServer(), new SuitsEnchantmentTagProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeClient(), new SuitsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new SuitsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new SuitsManualProvider(packOutput));
    }
}
