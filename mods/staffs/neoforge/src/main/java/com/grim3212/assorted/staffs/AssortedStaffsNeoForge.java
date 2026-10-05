package com.grim3212.assorted.staffs;

import com.grim3212.assorted.lib.data.ForgeBlockTagProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
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
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedStaffsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedStaffsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        NeoForgeFrozenStorage.ATTACHMENT_TYPES.register(modBus);
        NeoForgeFrozenStorage.aliasOldId();
        NeoForge.EVENT_BUS.addListener((EntityTickEvent.Post event) -> {
            if (!event.getEntity().level().isClientSide()) {
                FrozenMobs.thawIfBurning(event.getEntity());
            }
        });

        StaffsCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new StaffsRecipes.Runner(packOutput, lookupProvider));
        ForgeBlockTagProvider blockTagProvider = event.addProvider(new ForgeBlockTagProvider(packOutput, lookupProvider, Constants.MOD_ID, new StaffsBlockTagProvider(packOutput, lookupProvider)));
        event.addProvider(new ForgeItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), Constants.MOD_ID, new StaffsItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter())));
        event.addProvider(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(registries -> new StaffsChestLoot(), LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(StaffsEntityLoot::new, LootContextParamSets.ENTITY)), lookupProvider));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new StaffsItemModelProvider(packOutput));
        event.addProvider(new StaffsLanguageProvider(packOutput));
        event.addProvider(new StaffsManualProvider(packOutput));
    }
}
