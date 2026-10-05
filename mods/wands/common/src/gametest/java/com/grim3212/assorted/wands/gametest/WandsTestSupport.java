package com.grim3212.assorted.wands.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.wands.Constants;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Helpers shared by Assorted Wands's gametest classes, which import them statically, alongside
 * AssortedLib's {@code TestSupport}.
 */
final class WandsTestSupport {

    private WandsTestSupport() {
    }

    static boolean resourceExists(String path) {
        try (InputStream in = WandsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
    }

    /** A json the mod ships, read off the classpath rather than through a resource pack. */
    static JsonObject readJson(GameTestHelper helper, String path) {
        try (InputStream in = WandsTestSupport.class.getResourceAsStream(path)) {
            helper.assertTrue(in != null, path + " is not on the classpath");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw helper.assertionException("could not read " + path + ": " + e);
        }
    }

    static void removeFromInventory(ServerPlayer player, Item item) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).is(item)) {
                player.getInventory().setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    static JsonObject readLang(GameTestHelper helper) {
        return readJson(helper, "/assets/" + Constants.MOD_ID + "/lang/en_us.json");
    }
}
