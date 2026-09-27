package com.grim3212.assorted.buckets.data;

import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
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

public class BucketsItemTagProvider extends LibItemTagProvider {

    public BucketsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Vanilla's milk bucket too, so the cake recipe and anything else asking by tag takes every kind.
        add(appender, Items.MILK_BUCKET, LibCommonTags.Items.BUCKETS_MILK);
        for (BucketPair pair : BucketsItems.pairs()) {
            add(appender, pair.bucket().get(), LibCommonTags.Items.FLUID_CONTAINERS, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE);
            add(appender, pair.milk().get(), LibCommonTags.Items.BUCKETS_MILK);
        }
        add(appender, BucketsItems.GOLD.bucket().get(), ItemTags.PIGLIN_LOVED);
        add(appender, BucketsItems.GOLD.milk().get(), ItemTags.PIGLIN_LOVED);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
