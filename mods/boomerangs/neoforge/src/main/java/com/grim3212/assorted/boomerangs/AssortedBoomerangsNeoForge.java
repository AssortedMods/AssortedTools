package com.grim3212.assorted.boomerangs;

import com.grim3212.assorted.boomerangs.client.data.BoomerangsItemModelProvider;
import com.grim3212.assorted.boomerangs.client.data.BoomerangsLanguageProvider;
import com.grim3212.assorted.boomerangs.client.data.BoomerangsManualProvider;
import com.grim3212.assorted.boomerangs.data.BoomerangsDamageTypeTagProvider;
import com.grim3212.assorted.boomerangs.data.BoomerangsRecipes;
import com.grim3212.assorted.lib.data.ForgeDamageTypeTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedBoomerangsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedBoomerangsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        BoomerangsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new BoomerangsRecipes.Runner(packOutput, lookupProvider));
        event.addProvider(new ForgeDamageTypeTagsProvider(packOutput, lookupProvider, Constants.MOD_ID, new BoomerangsDamageTypeTagProvider(packOutput, lookupProvider)));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new BoomerangsItemModelProvider(packOutput));
        event.addProvider(new BoomerangsLanguageProvider(packOutput));
        event.addProvider(new BoomerangsManualProvider(packOutput));
    }
}
