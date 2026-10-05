package com.grim3212.assorted.suits.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.suits.Constants;
import net.minecraft.gametest.framework.GameTestHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Helpers shared by Assorted Suits's gametest classes, which import them statically, alongside
 * AssortedLib's {@code TestSupport}.
 */
final class SuitsTestSupport {

    private SuitsTestSupport() {
    }

    static boolean resourceExists(String path) {
        try (InputStream in = SuitsTestSupport.class.getResourceAsStream(path)) {
            return in != null;
        } catch (IOException e) {
            return false;
        }
    }

    /** A json the mod ships, read off the classpath rather than through a resource pack. */
    static JsonObject readJson(GameTestHelper helper, String path) {
        try (InputStream in = SuitsTestSupport.class.getResourceAsStream(path)) {
            helper.assertTrue(in != null, path + " is not on the classpath");
            return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException e) {
            throw helper.assertionException("could not read " + path + ": " + e);
        }
    }

    static JsonObject readLang(GameTestHelper helper) {
        return readJson(helper, "/assets/" + Constants.MOD_ID + "/lang/en_us.json");
    }
}
