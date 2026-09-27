package com.grim3212.assorted.machetes.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.machetes.api.MachetesTags;
import com.grim3212.assorted.machetes.common.item.MachetesItems;
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

public class MachetesItemTagProvider extends LibItemTagProvider {

    public MachetesItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Opt-in: without these a machete takes no enchantment at a table.
        for (Item machete : MachetesItems.machetes()) {
            add(appender, machete, MachetesTags.MACHETES, ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE,
                    ItemTags.FIRE_ASPECT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE);
        }
        // A machete is a sword to everything that asks by tag, Sweeping Edge included.
        appender.apply(ItemTags.SWORDS).addTag(MachetesTags.MACHETES);
        appender.apply(LibCommonTags.Items.TOOLS_MELEE_WEAPONS).addTag(MachetesTags.MACHETES);
        add(appender, MachetesItems.GOLD_MACHETE.get(), ItemTags.PIGLIN_LOVED);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
