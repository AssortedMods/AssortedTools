package com.grim3212.assorted.shears.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ShearsItemTagProvider extends LibItemTagProvider {

    public ShearsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Vanilla shears too: Coral Cutter used to go on anything that was a ShearsItem.
        add(appender, Items.SHEARS, ShearsTags.SHEARS_ENCHANTABLE);
        for (Item shears : ShearsItems.shears()) {
            add(appender, shears, LibCommonTags.Items.SHEARS, ShearsTags.SHEARS_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE);
        }
        add(appender, ShearsItems.GOLD_SHEARS.get(), ItemTags.PIGLIN_LOVED);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
