package com.grim3212.assorted.wands;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.wands.client.data.WandsItemModelProvider;
import com.grim3212.assorted.wands.client.data.WandsLanguageProvider;
import com.grim3212.assorted.wands.client.data.WandsManualProvider;
import com.grim3212.assorted.wands.data.WandsBlockTagProvider;
import com.grim3212.assorted.wands.data.WandsItemTagProvider;
import com.grim3212.assorted.wands.data.WandsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedWandsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedWandsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        WandsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new WandsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new WandsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new WandsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new WandsItemModelProvider(packOutput));
        event.addProvider(new WandsLanguageProvider(packOutput));
        event.addProvider(new WandsManualProvider(packOutput));
    }
}
