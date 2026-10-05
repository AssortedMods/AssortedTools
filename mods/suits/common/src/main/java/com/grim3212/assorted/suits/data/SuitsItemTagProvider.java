package com.grim3212.assorted.suits.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SuitsItemTagProvider extends LibItemTagProvider {

    public SuitsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Opt-in: without these the chicken suit takes no enchantment at a table, chicken jump included.
        armor(appender, ItemTags.HEAD_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_HELMET.get());
        armor(appender, ItemTags.CHEST_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_CHESTPLATE.get());
        armor(appender, ItemTags.LEG_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_LEGGINGS.get());
        armor(appender, ItemTags.FOOT_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_BOOTS.get());
    }

    private static void armor(Function<TagKey<Item>, TagAppender<Item>> appender, TagKey<Item> slotTag, Item item) {
        for (TagKey<Item> tag : List.of(slotTag, ItemTags.ARMOR_ENCHANTABLE, ItemTags.EQUIPPABLE_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE)) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
