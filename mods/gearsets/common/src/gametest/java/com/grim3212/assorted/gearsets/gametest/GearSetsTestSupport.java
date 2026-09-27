package com.grim3212.assorted.gearsets.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.lib.test.TestSupport;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Helpers shared by Assorted Gear Sets' gametest classes, which import them statically, alongside
 * AssortedLib's {@code TestSupport}.
 */
final class GearSetsTestSupport {

    private GearSetsTestSupport() {
    }

    static boolean resourceExists(String path) {
        try (InputStream in = GearSetsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
    }

    /** A json the mod ships, read off the classpath rather than through a resource pack. */
    static JsonObject readJson(GameTestHelper helper, String path) {
        try (InputStream in = GearSetsTestSupport.class.getResourceAsStream(path)) {
            helper.assertTrue(in != null, path + " is not on the classpath");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw helper.assertionException("could not read " + path + ": " + e);
        }
    }

    static JsonObject readLang(GameTestHelper helper) {
        return readJson(helper, "/assets/" + Constants.MOD_ID + "/lang/en_us.json");
    }

    /** The first item in {@code tag}, or empty when nothing in the game populates it. */
    static ItemStack firstOf(TagKey<Item> tag) {
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /** Asserts a grid crafts {@code expected} through the recipe manager, so a recipe dropped by a failing load condition fails here. */
    static void assertCrafts(GameTestHelper helper, int width, int height, List<ItemStack> grid, Item expected) {
        Identifier id = BuiltInRegistries.ITEM.getKey(expected);
        ItemStack result = TestSupport.craft(helper, CraftingInput.of(width, height, grid), "the pattern for " + id);
        helper.assertTrue(result.is(expected), "the pattern for " + id + " crafted " + BuiltInRegistries.ITEM.getKey(result.getItem()) + " instead");
    }

    static void assertSpeed(GameTestHelper helper, Item tool, BlockState state, float expected, String what) {
        helper.assertValueEqual(new ItemStack(tool).getDestroySpeed(state), expected, what);
    }

    static void expect(List<String> missing, TagKey<Item> tag, Item... items) {
        for (Item item : items) {
            if (!new ItemStack(item).is(tag)) {
                missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
            }
        }
    }

    static void assertArmorValue(GameTestHelper helper, ServerPlayer player, String name, Item helmet, Item chestplate, Item leggings, Item boots, int expected) {
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(helmet));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(chestplate));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(leggings));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));

        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(helmet)) == EquipmentSlot.HEAD, name + " helmet does not belong in the head slot");
        helper.assertTrue(player.getEquipmentSlotForItem(new ItemStack(boots)) == EquipmentSlot.FEET, name + " boots do not belong in the feet slot");

        // doTick, not tick: ServerPlayer#tick does not call super, so the equipment sweep only runs from doTick.
        player.doTick();

        helper.assertValueEqual(player.getArmorValue(), expected, "armor value while wearing a full set of " + name);
    }
}
