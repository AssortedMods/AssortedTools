package com.grim3212.assorted.boomerangs.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class BoomerangsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public BoomerangsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Both are made of vanilla materials, so neither needs a condition.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, BoomerangsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX")
                .unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output, recipeKey(BoomerangsItems.WOOD_BOOMERANG.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, BoomerangsItems.DIAMOND_BOOMERANG.get()).define('X', LibCommonTags.Items.GEMS_DIAMOND).define('Y', BoomerangsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX")
                .unlockedBy("has_diamonds", has(LibCommonTags.Items.GEMS_DIAMOND)).save(this.output, recipeKey(BoomerangsItems.DIAMOND_BOOMERANG.getId()));
    }

    private static ResourceKey<Recipe<?>> recipeKey(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new BoomerangsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
