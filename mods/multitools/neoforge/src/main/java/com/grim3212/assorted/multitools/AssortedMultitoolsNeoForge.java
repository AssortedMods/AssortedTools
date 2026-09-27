package com.grim3212.assorted.multitools;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.multitools.client.data.MultitoolsItemModelProvider;
import com.grim3212.assorted.multitools.client.data.MultitoolsLanguageProvider;
import com.grim3212.assorted.multitools.client.data.MultitoolsManualProvider;
import com.grim3212.assorted.multitools.data.MultitoolsBlockTagProvider;
import com.grim3212.assorted.multitools.data.MultitoolsItemTagProvider;
import com.grim3212.assorted.multitools.data.MultitoolsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedMultitoolsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedMultitoolsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        MultitoolsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new MultitoolsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new MultitoolsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new MultitoolsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new MultitoolsItemModelProvider(packOutput));
        event.addProvider(new MultitoolsLanguageProvider(packOutput));
        event.addProvider(new MultitoolsManualProvider(packOutput));
    }
}
