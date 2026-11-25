package com.grim3212.assorted.staffs;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.staffs.client.data.StaffsItemModelProvider;
import com.grim3212.assorted.staffs.client.data.StaffsLanguageProvider;
import com.grim3212.assorted.staffs.client.data.StaffsManualProvider;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import com.grim3212.assorted.staffs.data.StaffsBlockTagProvider;
import com.grim3212.assorted.staffs.data.StaffsChestLoot;
import com.grim3212.assorted.staffs.data.StaffsEntityLoot;
import com.grim3212.assorted.staffs.data.StaffsItemTagProvider;
import com.grim3212.assorted.staffs.data.StaffsRecipes;
import com.grim3212.assorted.staffs.platform.NeoForgeFrozenStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedStaffsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedStaffsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        NeoForgeFrozenStorage.ATTACHMENT_TYPES.register(modBus);
        NeoForgeFrozenStorage.aliasOldId();
        NeoForge.EVENT_BUS.addListener((EntityTickEvent.Post event) -> {
            if (!event.getEntity().level().isClientSide()) {
                FrozenMobs.thawIfBurning(event.getEntity());
            }
        });

        StaffsCommonMod.init();
    }

    /** One event for both halves; the data run's --server and --client flags pick which providers run. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(StaffsRecipes::new).loot(StaffsChestLoot::new, LootContextParamSets.CHEST).loot(StaffsEntityLoot::new, LootContextParamSets.ENTITY).addTo(event);
        ForgeBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(), new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new StaffsBlockTagProvider(packOutput, lookupProvider)));
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, fileHelper, new StaffsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));

        // Models, the language file and the manual, written into common for both loaders.
        generator.addProvider(event.includeClient(), new StaffsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new StaffsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new StaffsManualProvider(packOutput));
    }
}
