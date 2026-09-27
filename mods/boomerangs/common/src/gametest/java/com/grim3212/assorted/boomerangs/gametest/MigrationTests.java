package com.grim3212.assorted.boomerangs.gametest;

import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
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
        book.loadUntrusted(new ServerRecipeBook.Packed(new RecipeBookSettings(), List.of(recipe(Family.ID, "wood_boomerang")), List.of()),
                key -> helper.getLevel().recipeAccess().byKey(key).isPresent());
        helper.assertTrue(book.contains(recipe(Constants.MOD_ID, "wood_boomerang")), "the recipe a player had unlocked as " + Family.ID + ":wood_boomerang was not carried over");
        helper.succeed();
    }

    private static ResourceKey<Recipe<?>> recipe(String namespace, String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
