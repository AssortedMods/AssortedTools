package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.lib.data.LibItemTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.staffs.api.StaffsTags;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
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

public class StaffsItemTagProvider extends LibItemTagProvider {

    public StaffsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // Opt-in: without these the Neptune and Phoenix staffs take no weapon enchantment at a table.
        for (Item staff : List.of(StaffsItems.NEPTUNE_STAFF.get(), StaffsItems.PHOENIX_STAFF.get())) {
            add(appender, staff, ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE, LibCommonTags.Items.TOOLS_MELEE_WEAPONS);
        }
        add(appender, StaffsItems.FROST_ROD.get(), StaffsTags.RODS_FROST);
        appender.apply(LibCommonTags.Items.RODS).addTag(StaffsTags.RODS_FROST);
    }

    @SafeVarargs
    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, Item item, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            appender.apply(tag).add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
