package com.grim3212.assorted.ultimatefist.gametest;

import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.ultimatefist.Constants;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.ServerRecipeBook;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** A player's recipe book from when this was all one mod, Assorted Tools, keeps its recipes. */
final class MigrationTests {

    private MigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("assortedtools_recipe_book_carries_over", MigrationTests::recipeBookCarriesOver);
    }

    private static void recipeBookCarriesOver(GameTestHelper helper) {
        ServerRecipeBook book = TestSupport.survivalPlayer(helper).getRecipeBook();
        // Laid out the way a player's saved recipe book is.
        ListTag known = new ListTag();
        known.add(StringTag.valueOf(recipe(Constants.FAMILY_ID, "ultimate_fist").toString()));
        CompoundTag saved = new CompoundTag();
        saved.put("recipes", known);
        book.fromNbt(saved, helper.getLevel().getRecipeManager());
        helper.assertTrue(book.contains(recipe(Constants.MOD_ID, "ultimate_fist")), "the recipe a player had unlocked as " + Constants.FAMILY_ID + ":ultimate_fist was not carried over");
        helper.succeed();
    }

    private static ResourceLocation recipe(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
