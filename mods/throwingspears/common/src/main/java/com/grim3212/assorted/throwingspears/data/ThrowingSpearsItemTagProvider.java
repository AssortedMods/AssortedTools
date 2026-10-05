package com.grim3212.assorted.throwingspears.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.throwingspears.common.enchantment.ThrowingSpearsEnchantments;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
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

public class ThrowingSpearsItemTagProvider extends LibItemTagProvider {

    public ThrowingSpearsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // The trident tag brings Loyalty and Impaling; BetterSpearItem vetoes its Riptide and Channeling by key.
        for (Item spear : ThrowingSpearsItems.spears()) {
            add(appender, spear, ThrowingSpearsEnchantments.SPEAR_ENCHANTABLE, ItemTags.TRIDENT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE);
        }
        add(appender, ThrowingSpearsItems.GOLD_THROWING_SPEAR.get(), ItemTags.PIGLIN_LOVED);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
