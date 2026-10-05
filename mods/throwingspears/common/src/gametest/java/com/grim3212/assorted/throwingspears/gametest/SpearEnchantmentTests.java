package com.grim3212.assorted.throwingspears.gametest;

import com.grim3212.assorted.throwingspears.ThrowingSpearsCommonMod;
import com.grim3212.assorted.throwingspears.common.enchantment.ThrowingSpearsEnchantments;
import com.grim3212.assorted.throwingspears.common.item.BetterSpearItem;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** The four spear enchantments exist and are obtainable, and a spear takes a trident's enchantments but Riptide and Channeling. */
final class SpearEnchantmentTests {

    private static final List<ResourceKey<Enchantment>> SPEAR_ENCHANTMENTS = List.of(ThrowingSpearsEnchantments.BOUNCINESS, ThrowingSpearsEnchantments.CONDUCTIVE, ThrowingSpearsEnchantments.FLAMMABLE, ThrowingSpearsEnchantments.UNSTABLE);

    private SpearEnchantmentTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("spears_take_the_right_enchantments", SpearEnchantmentTests::spearsTakeTheRightEnchantments);
        out.accept("spear_enchantments_are_obtainable", SpearEnchantmentTests::spearEnchantmentsAreObtainable);
        out.accept("spears_are_never_offered_riptide_or_channeling", SpearEnchantmentTests::spearsAreNeverOfferedRiptideOrChanneling);
        out.accept("spear_anvil_rejects_riptide_and_channeling", SpearEnchantmentTests::spearAnvilRejectsRiptideAndChanneling);
        out.accept("conductivity_chances_read_back_as_floats", SpearEnchantmentTests::conductivityChancesReadBackAsFloats);
    }

    private static void spearsTakeTheRightEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (BetterSpearItem spear : ThrowingSpearsItems.spears()) {
            for (TagKey<Item> tag : List.of(ThrowingSpearsEnchantments.SPEAR_ENCHANTABLE, ItemTags.TRIDENT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE)) {
                if (!new ItemStack(spear).is(tag)) {
                    missing.add(BuiltInRegistries.ITEM.getKey(spear) + " not in #" + tag.location());
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    private static void spearEnchantmentsAreObtainable(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        List<String> missing = new ArrayList<>();

        for (ResourceKey<Enchantment> key : SPEAR_ENCHANTMENTS) {
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(key);
            if (enchantment.isEmpty()) {
                missing.add(key.identifier() + " (not registered)");
                continue;
            }

            for (TagKey<Enchantment> tag : List.of(EnchantmentTags.IN_ENCHANTING_TABLE, EnchantmentTags.TRADEABLE, EnchantmentTags.ON_RANDOM_LOOT)) {
                if (!enchantment.get().is(tag)) {
                    missing.add(key.identifier() + " not in #" + tag.location());
                }
            }
        }

        helper.assertTrue(missing.isEmpty(), "enchantments missing or not obtainable: " + String.join(", ", missing));
        helper.succeed();
    }

    /**
     * Asks the way the enchanting table does, so each loader's wiring is under test. A cost of 30 falls in every
     * trident and spear enchantment's window; the vanilla trident is checked so the veto cannot leak onto it.
     */
    private static void spearsAreNeverOfferedRiptideOrChanneling(GameTestHelper helper) {
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Set<ResourceKey<Enchantment>> spear = offeredAtTable(registry, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()));
        Set<ResourceKey<Enchantment>> trident = offeredAtTable(registry, new ItemStack(Items.TRIDENT));

        helper.assertFalse(spear.contains(Enchantments.RIPTIDE) || spear.contains(Enchantments.CHANNELING), "a spear was offered riptide or channeling: " + spear);
        helper.assertTrue(spear.containsAll(List.of(Enchantments.LOYALTY, Enchantments.IMPALING)), "a spear was not offered loyalty and impaling: " + spear);
        helper.assertTrue(spear.containsAll(SPEAR_ENCHANTMENTS), "a spear was not offered all four spear enchantments: " + spear);
        helper.assertTrue(trident.containsAll(List.of(Enchantments.RIPTIDE, Enchantments.CHANNELING)), "the vanilla trident lost riptide or channeling: " + trident);
        helper.succeed();
    }

    /** Through a real {@link AnvilMenu} so each loader's hook runs; a vanilla trident still takes Riptide. */
    private static void spearAnvilRejectsRiptideAndChanneling(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        Registry<Enchantment> registry = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> riptide = registry.getOrThrow(Enchantments.RIPTIDE);
        Holder<Enchantment> loyalty = registry.getOrThrow(Enchantments.LOYALTY);

        helper.assertTrue(combine(player, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()), riptide).isEmpty(), "riptide went onto a spear at an anvil");
        helper.assertTrue(combine(player, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()), registry.getOrThrow(Enchantments.CHANNELING)).isEmpty(), "channeling went onto a spear at an anvil");

        ItemStack loyalSpear = combine(player, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()), loyalty);
        helper.assertValueEqual(loyalSpear.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(loyalty), 1, "loyalty level on a spear from an anvil");

        ItemStack riptideTrident = combine(player, new ItemStack(Items.TRIDENT), riptide);
        helper.assertValueEqual(riptideTrident.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(riptide), 1, "riptide level on a vanilla trident from an anvil");
        helper.succeed();
    }

    private static void conductivityChancesReadBackAsFloats(GameTestHelper helper) {
        List<? extends Float> chances = ThrowingSpearsCommonMod.COMMON_CONFIG.conductivityLightningChances.get();
        helper.assertTrue(chances != null && !chances.isEmpty(), "the configured conductivity chances are missing");

        for (Object chance : chances) {
            helper.assertTrue(chance instanceof Float, "a Float list option handed back a " + chance.getClass().getSimpleName() + " (" + chance + "); every read of it unboxes to float");
        }

        // Every entry is a chance, which is what the spear falls back to its defaults over.
        for (float chance : chances) {
            helper.assertTrue(chance >= 0.0F && chance < 1.0F, "a configured conductivity chance of " + chance + " is outside [0, 1)");
        }

        helper.succeed();
    }

    private static Set<ResourceKey<Enchantment>> offeredAtTable(Registry<Enchantment> registry, ItemStack stack) {
        return EnchantmentHelper.getAvailableEnchantmentResults(30, stack, registry.getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE).stream())
                .stream().map(instance -> instance.enchantment().unwrapKey().orElseThrow()).collect(Collectors.toSet());
    }

    private static ItemStack combine(ServerPlayer player, ItemStack left, Holder<Enchantment> enchantment) {
        ItemEnchantments.Mutable stored = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        stored.set(enchantment, 1);
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        book.set(DataComponents.STORED_ENCHANTMENTS, stored.toImmutable());

        AnvilMenu menu = new AnvilMenu(0, player.getInventory());
        menu.getSlot(AnvilMenu.INPUT_SLOT).set(left);
        menu.getSlot(AnvilMenu.ADDITIONAL_SLOT).set(book);
        menu.createResult();
        return menu.getSlot(AnvilMenu.RESULT_SLOT).getItem();
    }
}
