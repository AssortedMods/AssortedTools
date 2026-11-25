package com.grim3212.assorted.multitools;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.multitools.client.data.MultitoolsItemModelProvider;
import com.grim3212.assorted.multitools.client.data.MultitoolsLanguageProvider;
import com.grim3212.assorted.multitools.client.data.MultitoolsManualProvider;
import com.grim3212.assorted.multitools.data.MultitoolsBlockTagProvider;
import com.grim3212.assorted.multitools.data.MultitoolsItemTagProvider;
import com.grim3212.assorted.multitools.data.MultitoolsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedMultitoolsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedMultitoolsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        MultitoolsCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(MultitoolsRecipes::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new MultitoolsBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new MultitoolsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new MultitoolsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new MultitoolsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new MultitoolsManualProvider(packOutput));
    }
}
