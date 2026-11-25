package com.grim3212.assorted.boomerangs;

import com.grim3212.assorted.boomerangs.client.data.BoomerangsItemModelProvider;
import com.grim3212.assorted.boomerangs.client.data.BoomerangsLanguageProvider;
import com.grim3212.assorted.boomerangs.client.data.BoomerangsManualProvider;
import com.grim3212.assorted.boomerangs.data.BoomerangsDamageTypeTagProvider;
import com.grim3212.assorted.boomerangs.data.BoomerangsRecipes;
import com.grim3212.assorted.lib.data.ForgeDamageTypeTagsProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
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
public class AssortedBoomerangsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedBoomerangsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        BoomerangsCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(BoomerangsRecipes::new).addTo(event);
        generator.addProvider(event.includeServer(), new ForgeDamageTypeTagsProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new BoomerangsDamageTypeTagProvider(packOutput, lookupProvider)));

        generator.addProvider(event.includeClient(), new BoomerangsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new BoomerangsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new BoomerangsManualProvider(packOutput));
    }
}
