package com.grim3212.assorted.shears.gametest;

import com.google.gson.JsonParser;
import com.grim3212.assorted.shears.Family;
import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import com.grim3212.assorted.shears.common.item.MaterialShears;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Coral Cutter can be found, and every pair of shears can take it and the durability enchantments. */
final class CoralCutterTests {

    private CoralCutterTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("coral_cutter_is_obtainable", CoralCutterTests::coralCutterIsObtainable);
        out.accept("shears_take_the_right_enchantments", CoralCutterTests::shearsTakeTheRightEnchantments);
        out.accept("assortedtools_coral_cutter_carries_over", CoralCutterTests::assortedtoolsCoralCutterCarriesOver);
    }

    private static void coralCutterIsObtainable(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Optional<Holder.Reference<Enchantment>> coralCutter = registry.get(ShearsEnchantments.CORAL_CUTTER);
        helper.assertTrue(coralCutter.isPresent(), ShearsEnchantments.CORAL_CUTTER.identifier() + " is not registered");

        List<String> missing = new ArrayList<>();
        for (TagKey<Enchantment> tag : List.of(EnchantmentTags.IN_ENCHANTING_TABLE, EnchantmentTags.TRADEABLE, EnchantmentTags.ON_RANDOM_LOOT)) {
            if (!coralCutter.get().is(tag)) {
                missing.add("#" + tag.location());
            }
        }
        helper.assertTrue(missing.isEmpty(), "coral cutter is not in " + String.join(", ", missing));
        helper.succeed();
    }

    private static void shearsTakeTheRightEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        expect(missing, ShearsTags.SHEARS_ENCHANTABLE, Items.SHEARS);
        for (MaterialShears shears : ShearsItems.shears()) {
            expect(missing, ShearsTags.SHEARS_ENCHANTABLE, shears);
            expect(missing, ItemTags.DURABILITY_ENCHANTABLE, shears);
            expect(missing, ItemTags.VANISHING_ENCHANTABLE, shears);
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    /** Shears saved with Coral Cutter under its old id, read the way a chest reads its items. */
    private static void assortedtoolsCoralCutterCarriesOver(GameTestHelper helper) {
        Holder<Enchantment> coralCutter = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ShearsEnchantments.CORAL_CUTTER);
        String saved = "{\"id\": \"" + Family.ID + ":diamond_shears\", \"count\": 1, \"components\": {\"minecraft:enchantments\": {\"" + Family.ID + ":coral_cutter\": 1}}}";
        ItemStack stack = ItemStack.CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, helper.getLevel().registryAccess()), JsonParser.parseString(saved)).resultOrPartial(error -> {
        }).orElse(ItemStack.EMPTY);

        helper.assertTrue(stack.is(ShearsItems.DIAMOND_SHEARS.get()), "old diamond shears read back as " + stack);
        helper.assertValueEqual(stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(coralCutter), 1, "coral cutter level on old shears");
        helper.succeed();
    }

    private static void expect(List<String> missing, TagKey<Item> tag, Item item) {
        if (!new ItemStack(item).is(tag)) {
            missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
        }
    }
}
