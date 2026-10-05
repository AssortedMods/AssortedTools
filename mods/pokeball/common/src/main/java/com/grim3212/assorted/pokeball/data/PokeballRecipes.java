package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class PokeballRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public PokeballRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Nothing here needs another mod's material.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, PokeballItems.POKEBALL.get()).define('R', LibCommonTags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', LibCommonTags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON)
                .pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE))
                .save(this.output, ResourceKey.create(Registries.RECIPE, id(PokeballItems.POKEBALL.get())));
    }

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new PokeballRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
