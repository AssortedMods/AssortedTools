package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.lib.data.ForgeDamageTypeTagsProvider;
import com.grim3212.assorted.lib.data.ForgeItemTagProvider;
import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsItemModelProvider;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsLanguageProvider;
import com.grim3212.assorted.throwingspears.client.data.ThrowingSpearsManualProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsDamageTypeTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsEnchantmentData;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsEnchantmentTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsItemTagProvider;
import com.grim3212.assorted.throwingspears.data.ThrowingSpearsRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Constants.MOD_ID)
public class AssortedThrowingSpearsNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedThrowingSpearsNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        ThrowingSpearsCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        ForgeRegistryEntries.of(Constants.MOD_ID).world(new ThrowingSpearsEnchantmentData()).recipes(ThrowingSpearsRecipes::new).addTo(event);
        // No blocks, so no block tags for the item tags to copy.
        CompletableFuture<TagsProvider.TagLookup<Block>> noBlockTags = CompletableFuture.completedFuture(TagsProvider.TagLookup.empty());
        generator.addProvider(event.includeServer(), new ForgeItemTagProvider(packOutput, lookupProvider, noBlockTags, Constants.MOD_ID, fileHelper, new ThrowingSpearsItemTagProvider(packOutput, lookupProvider, noBlockTags)));
        // Enchantments are datapack registry content, not registered from code.
        generator.addProvider(event.includeServer(), new ThrowingSpearsEnchantmentTagProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ForgeDamageTypeTagsProvider(packOutput, lookupProvider, Constants.MOD_ID, fileHelper, new ThrowingSpearsDamageTypeTagProvider(packOutput, lookupProvider)));

        generator.addProvider(event.includeClient(), new ThrowingSpearsItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ThrowingSpearsLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ThrowingSpearsManualProvider(packOutput));
    }
}
