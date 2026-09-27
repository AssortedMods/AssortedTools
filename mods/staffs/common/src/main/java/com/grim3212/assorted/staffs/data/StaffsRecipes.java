package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.api.StaffsTags;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class StaffsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public StaffsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Made from vanilla things and this part's own, so nothing to condition.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        // The blaze rod, blaze powder and fire charge chain, cold. Each staff takes its own charge.
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.BREWING, StaffsItems.FROST_POWDER.get(), 2).requires(StaffsTags.RODS_FROST).unlockedBy("has_frost_rod", has(StaffsTags.RODS_FROST)).save(this.output, recipeKey(StaffsItems.FROST_POWDER.getId()));
        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, StaffsItems.ICE_CHARGE.get(), 3).requires(LibCommonTags.Items.GUNPOWDER).requires(StaffsItems.FROST_POWDER.get()).requires(Items.SNOWBALL).unlockedBy("has_frost_powder", has(StaffsItems.FROST_POWDER.get())).save(this.output, recipeKey(StaffsItems.ICE_CHARGE.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, StaffsItems.NEPTUNE_STAFF.get()).define('D', LibCommonTags.Items.GEMS_DIAMOND).define('C', StaffsItems.ICE_CHARGE.get()).define('S', StaffsTags.RODS_FROST).pattern("D").pattern("C").pattern("S").unlockedBy("has_frost_rod", has(StaffsTags.RODS_FROST)).save(this.output, recipeKey(StaffsItems.NEPTUNE_STAFF.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, StaffsItems.PHOENIX_STAFF.get()).define('D', LibCommonTags.Items.GEMS_DIAMOND).define('C', Items.FIRE_CHARGE).define('S', LibCommonTags.Items.RODS_BLAZE).pattern("D").pattern("C").pattern("S").unlockedBy("has_blaze_rod", has(LibCommonTags.Items.RODS_BLAZE)).save(this.output, recipeKey(StaffsItems.PHOENIX_STAFF.getId()));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, StaffsItems.POWER_STAFF.get()).define('I', LibCommonTags.Items.INGOTS_IRON).define('D', LibCommonTags.Items.GEMS_DIAMOND).define('R', LibCommonTags.Items.DUSTS_REDSTONE).pattern("IDI").pattern("IRI").pattern(" I ").unlockedBy("has_diamond", has(LibCommonTags.Items.GEMS_DIAMOND)).save(this.output, recipeKey(StaffsItems.POWER_STAFF.getId()));
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
            return new StaffsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
