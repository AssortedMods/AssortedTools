package com.grim3212.assorted.shears;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeDatapackRegistryProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.shears.client.data.ShearsItemModelProvider;
import com.grim3212.assorted.shears.client.data.ShearsLanguageProvider;
import com.grim3212.assorted.shears.client.data.ShearsManualProvider;
import com.grim3212.assorted.shears.data.ShearsBlockTagProvider;
import com.grim3212.assorted.shears.data.ShearsEnchantmentData;
import com.grim3212.assorted.shears.data.ShearsEnchantmentTagProvider;
import com.grim3212.assorted.shears.data.ShearsItemTagProvider;
import com.grim3212.assorted.shears.data.ShearsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedShearsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedShearsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        ShearsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ShearsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new ShearsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new ShearsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        // Enchantments are datapack registry content, not registered from code.
        event.addProvider(new ForgeDatapackRegistryProvider(Constants.MOD_ID, new ShearsEnchantmentData()).datpackEntriesProvider(packOutput, lookupProvider));
        event.addProvider(new ShearsEnchantmentTagProvider(packOutput, lookupProvider));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new ShearsItemModelProvider(packOutput));
        event.addProvider(new ShearsLanguageProvider(packOutput));
        event.addProvider(new ShearsManualProvider(packOutput));
    }
}
