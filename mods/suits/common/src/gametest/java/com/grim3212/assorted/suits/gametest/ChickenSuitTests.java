package com.grim3212.assorted.suits.gametest;

import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import com.grim3212.assorted.suits.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** Chicken jump is obtainable, the chicken suit takes it at a table, and an anvil moves it onto other armor. */
final class ChickenSuitTests {

    private ChickenSuitTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("chicken_suit_converts_armor_in_an_anvil", ChickenSuitTests::chickenSuitConvertsArmorInAnAnvil);
        out.accept("chicken_jump_is_obtainable", ChickenSuitTests::chickenJumpIsObtainable);
        out.accept("chicken_suit_takes_armor_enchantments", ChickenSuitTests::chickenSuitTakesArmorEnchantments);
        out.accept("jei_plugin_is_registered_on_fabric", ChickenSuitTests::jeiPluginIsRegisteredOnFabric);
    }

    private static void chickenSuitConvertsArmorInAnAnvil(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        AnvilUpdatedEvent matching = new AnvilUpdatedEvent(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(SuitsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(matching);

        ItemStack output = matching.getOutput();
        helper.assertTrue(output.is(Items.IRON_CHESTPLATE), "converting an iron chestplate gave back " + output);
        helper.assertValueEqual(SuitsEnchantments.getLevel(output, SuitsEnchantments.CHICKEN_JUMP), 1, "chicken jump level on the converted armor");
        helper.assertValueEqual(matching.getMaterialCost(), 1, "chicken suit pieces consumed");
        helper.assertValueEqual(matching.getCost(), 5, "level cost of converting a chestplate");

        // A helmet against a chestplate is the wrong slot and has to be left alone.
        AnvilUpdatedEvent mismatched = new AnvilUpdatedEvent(new ItemStack(Items.IRON_HELMET), new ItemStack(SuitsItems.CHICKEN_SUIT_CHESTPLATE.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(mismatched);
        helper.assertTrue(mismatched.getOutput().isEmpty(), "a chicken suit chestplate converted a helmet");

        AnvilUpdatedEvent notArmor = new AnvilUpdatedEvent(new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(SuitsItems.CHICKEN_SUIT_HELMET.get()), "", 0, player);
        ChickenSuitConversionHandler.anvilUpdateEvent(notArmor);
        helper.assertTrue(notArmor.getOutput().isEmpty(), "a chicken suit helmet converted a pickaxe");

        helper.succeed();
    }

    private static void chickenJumpIsObtainable(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Optional<Holder.Reference<Enchantment>> enchantment = registry.get(SuitsEnchantments.CHICKEN_JUMP);
        helper.assertTrue(enchantment.isPresent(), SuitsEnchantments.CHICKEN_JUMP.identifier() + " is not registered");

        List<String> missing = new ArrayList<>();
        for (TagKey<Enchantment> tag : List.of(EnchantmentTags.IN_ENCHANTING_TABLE, EnchantmentTags.TRADEABLE, EnchantmentTags.ON_RANDOM_LOOT)) {
            if (!enchantment.get().is(tag)) {
                missing.add("#" + tag.location());
            }
        }
        helper.assertTrue(missing.isEmpty(), "chicken jump is not in " + String.join(", ", missing));
        helper.succeed();
    }

    private static void chickenSuitTakesArmorEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        expectArmor(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_HELMET.get());
        expectArmor(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_CHESTPLATE.get());
        expectArmor(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_LEGGINGS.get());
        expectArmor(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, SuitsItems.CHICKEN_SUIT_BOOTS.get());
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    private static void jeiPluginIsRegisteredOnFabric(GameTestHelper helper) {
        TestSupport.assertJeiPluginIsRegistered(helper, Constants.MOD_ID, "com.grim3212.assorted.suits.compat.jei.JEIAssortedSuits");
        helper.succeed();
    }

    private static void expectArmor(List<String> missing, TagKey<Item> slotTag, Item item) {
        for (TagKey<Item> tag : List.of(slotTag, ItemTags.ARMOR_ENCHANTABLE, ItemTags.EQUIPPABLE_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE)) {
            if (!new ItemStack(item).is(tag)) {
                missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
            }
        }
    }
}
