package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.lib.data.ForgeDamageTypeTagsProvider;
import com.grim3212.assorted.lib.data.ForgeDatapackRegistryProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsItemModelProvider;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsLanguageProvider;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsManualProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsDamageTypeTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsEnchantmentData;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsEnchantmentTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsItemTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsRecipes;
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
public class AssortedThrowingSpearsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedThrowingSpearsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        ThrowingSpearsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ThrowingSpearsRecipes.Runner(packOutput, lookupProvider));
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, new ThrowingSpearsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
        // Enchantments are datapack registry content, not registered from code.
        event.addProvider(new ForgeDatapackRegistryProvider(Constants.MOD_ID, new ThrowingSpearsEnchantmentData()).datpackEntriesProvider(packOutput, lookupProvider));
        event.addProvider(new ThrowingSpearsEnchantmentTagProvider(packOutput, lookupProvider));
        event.addProvider(new ForgeDamageTypeTagsProvider(packOutput, lookupProvider, Constants.MOD_ID, new ThrowingSpearsDamageTypeTagProvider(packOutput, lookupProvider)));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new ThrowingSpearsItemModelProvider(packOutput));
        event.addProvider(new ThrowingSpearsLanguageProvider(packOutput));
        event.addProvider(new ThrowingSpearsManualProvider(packOutput));
    }
}
