package com.grim3212.assorted.extramaterials.data;

import com.grim3212.assorted.extramaterials.api.ExtraMaterialsTags;
import com.grim3212.assorted.extramaterials.common.item.ExtraMaterialsItems;
import com.grim3212.assorted.extramaterials.common.item.MaterialSet;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ExtraMaterialsItemTagProvider extends LibItemTagProvider {

    /** Vanilla's tools by material, named as the convention tags name materials: wood, not wooden. */
    private static final Map<String, Item[]> VANILLA_TOOLS = Map.of(
            "wood", new Item[]{Items.WOODEN_SWORD, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SPEAR},
            "stone", new Item[]{Items.STONE_SWORD, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SPEAR},
            "copper", new Item[]{Items.COPPER_SWORD, Items.COPPER_PICKAXE, Items.COPPER_SHOVEL, Items.COPPER_AXE, Items.COPPER_HOE, Items.COPPER_SPEAR},
            "iron", new Item[]{Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SPEAR},
            "gold", new Item[]{Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SPEAR},
            "diamond", new Item[]{Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SPEAR},
            "netherite", new Item[]{Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SPEAR});

    public ExtraMaterialsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags);
    }

    /** Every material given a c:<kind>/<material> tag here, once each, for the language provider to name; copper is both vanilla's and one of ours. */
    public static Set<String> materialToolNames() {
        Set<String> materials = new TreeSet<>(VANILLA_TOOLS.keySet());
        materials.addAll(ExtraMaterialsItems.MATERIALS.keySet());
        return materials;
    }

    @Override
    public void addCommonTags(Function<TagKey<Item>, TagAppender<Item>> appender, BiConsumer<TagKey<Block>, TagKey<Item>> copier) {
        // c:<kind>/<material>, in the order of MATERIAL_TOOL_KINDS.
        VANILLA_TOOLS.forEach((material, tools) -> materialTools(appender, material, tools));

        for (MaterialSet set : ExtraMaterialsItems.MATERIALS.values()) {
            materialTools(appender, set.name(), set.sword().get(), set.pickaxe().get(), set.shovel().get(), set.axe().get(), set.hoe().get(), set.spear().get());

            add(appender, ItemTags.SWORDS, set.sword().get());
            add(appender, ItemTags.PICKAXES, set.pickaxe().get());
            add(appender, ItemTags.SHOVELS, set.shovel().get());
            add(appender, ItemTags.AXES, set.axe().get());
            add(appender, ItemTags.HOES, set.hoe().get());
            // Where vanilla puts its own spears: the spear tag, piglins' preferred weapons, and the lunge enchantment.
            add(appender, ItemTags.SPEARS, set.spear().get());
            add(appender, ItemTags.PIGLIN_PREFERRED_WEAPONS, set.spear().get());
            add(appender, ItemTags.LUNGE_ENCHANTABLE, set.spear().get());
            // Other mods recognise a weapon or a mining tool by these, which both loaders fill with vanilla items only.
            add(appender, LibCommonTags.Items.TOOLS_MELEE_WEAPONS, set.sword().get(), set.axe().get(), set.spear().get());
            add(appender, LibCommonTags.Items.TOOLS_MINING_TOOLS, set.pickaxe().get());
            add(appender, ItemTags.HEAD_ARMOR, set.helmet().get());
            add(appender, ItemTags.CHEST_ARMOR, set.chestplate().get());
            add(appender, ItemTags.LEG_ARMOR, set.leggings().get());
            add(appender, ItemTags.FOOT_ARMOR, set.boots().get());

            // Opt-in: without these a tool, weapon or armor piece takes no enchantment at a table.
            add(appender, ItemTags.MINING_ENCHANTABLE, set.pickaxe().get(), set.shovel().get(), set.axe().get(), set.hoe().get());
            add(appender, ItemTags.MINING_LOOT_ENCHANTABLE, set.pickaxe().get(), set.shovel().get(), set.axe().get(), set.hoe().get());
            for (TagKey<Item> tag : List.of(ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE)) {
                add(appender, tag, set.sword().get(), set.axe().get(), set.spear().get());
            }
            armor(appender, ItemTags.HEAD_ARMOR_ENCHANTABLE, set.helmet().get());
            armor(appender, ItemTags.CHEST_ARMOR_ENCHANTABLE, set.chestplate().get());
            armor(appender, ItemTags.LEG_ARMOR_ENCHANTABLE, set.leggings().get());
            armor(appender, ItemTags.FOOT_ARMOR_ENCHANTABLE, set.boots().get());
            for (Item item : set.items()) {
                add(appender, ItemTags.DURABILITY_ENCHANTABLE, item);
                add(appender, ItemTags.VANISHING_ENCHANTABLE, item);
            }
        }
    }

    private static void armor(Function<TagKey<Item>, TagAppender<Item>> appender, TagKey<Item> slotTag, Item item) {
        add(appender, slotTag, item);
        add(appender, ItemTags.ARMOR_ENCHANTABLE, item);
        add(appender, ItemTags.EQUIPPABLE_ENCHANTABLE, item);
    }

    private static void materialTools(Function<TagKey<Item>, TagAppender<Item>> appender, String material, Item... tools) {
        for (int i = 0; i < tools.length; i++) {
            add(appender, ExtraMaterialsTags.materialTools(ExtraMaterialsTags.MATERIAL_TOOL_KINDS.get(i), material), tools[i]);
        }
    }

    private static void add(Function<TagKey<Item>, TagAppender<Item>> appender, TagKey<Item> tag, Item... items) {
        TagAppender<Item> tagger = appender.apply(tag);
        for (Item item : items) {
            tagger.add(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
        }
    }
}
