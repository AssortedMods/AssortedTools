package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PokeballItemTagProvider extends LibItemTagProvider {

    /** Assorted Decor's cages hold what is in this tag; filled here so a pokeball fits one whether or not Decor is installed. */
    public static final TagKey<Item> CAGE_SUPPORTED = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("assorteddecor", "cage_supported"));

    public PokeballItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        appender.apply(CAGE_SUPPORTED).add(BuiltInRegistries.ITEM.getResourceKey(PokeballItems.POKEBALL.get()).orElseThrow());
    }
}
