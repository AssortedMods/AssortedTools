package com.grim3212.assorted.wands;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.wands.client.data.WandsItemModelProvider;
import com.grim3212.assorted.wands.client.data.WandsLanguageProvider;
import com.grim3212.assorted.wands.client.data.WandsManualProvider;
import com.grim3212.assorted.wands.data.WandsBlockTagProvider;
import com.grim3212.assorted.wands.data.WandsItemTagProvider;
import com.grim3212.assorted.wands.data.WandsRecipes;
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
public class AssortedWandsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedWandsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        WandsCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(WandsRecipes::new).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new WandsBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new WandsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        generator.addProvider(event.includeClient(), new WandsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new WandsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new WandsManualProvider(packOutput));
    }
}
