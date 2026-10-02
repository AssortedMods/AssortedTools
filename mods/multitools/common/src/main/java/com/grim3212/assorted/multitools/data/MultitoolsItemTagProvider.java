package com.grim3212.assorted.multitools.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MultitoolsItemTagProvider extends LibItemTagProvider {

    public MultitoolsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Opt-in: without these a multitool takes no enchantment at a table. Other mods know a weapon or mining tool by the c: tags.
        for (Item multitool : MultitoolsItems.multitools()) {
            add(appender, multitool, ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE,
                    ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE,
                    LibCommonTags.Items.TOOLS_MELEE_WEAPONS, LibCommonTags.Items.TOOLS_MINING_TOOLS);
            // In every tool tag, so anything asking for a sword, pickaxe, axe, shovel or hoe takes a multitool. Vanilla, and
            // so Fabric, sweeps only with #swords, and the grinding mill's tool slot asks for a #pickaxes of a tier.
            add(appender, multitool, ItemTags.SWORDS, ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES);
        }
        add(appender, MultitoolsItems.GOLDEN_MULTITOOL.get(), ItemTags.PIGLIN_LOVED);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
