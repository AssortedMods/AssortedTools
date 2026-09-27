package com.grim3212.assorted.wands.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.common.item.WandsItems;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class WandsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public WandsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Made from vanilla things only, so nothing to condition.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        wand(WandsItems.BUILDING_WAND, ItemTags.PLANKS, LibCommonTags.Items.INGOTS_GOLD, "has_gold");
        wand(WandsItems.BREAKING_WAND, ItemTags.PLANKS, LibCommonTags.Items.INGOTS_IRON, "has_iron");
        wand(WandsItems.MINING_WAND, ItemTags.PLANKS, LibCommonTags.Items.GEMS_DIAMOND, "has_diamond");
        wand(WandsItems.REINFORCED_BUILDING_WAND, LibCommonTags.Items.OBSIDIAN, LibCommonTags.Items.STORAGE_BLOCKS_GOLD, "has_obsidian");
        wand(WandsItems.REINFORCED_BREAKING_WAND, LibCommonTags.Items.OBSIDIAN, LibCommonTags.Items.STORAGE_BLOCKS_IRON, "has_obsidian");
        wand(WandsItems.REINFORCED_MINING_WAND, LibCommonTags.Items.OBSIDIAN, LibCommonTags.Items.STORAGE_BLOCKS_DIAMOND, "has_obsidian");
    }

    private void wand(IRegistryObject<? extends Item> wand, TagKey<Item> shaft, TagKey<Item> core, String unlock) {
        TagKey<Item> unlockedBy = unlock.equals("has_obsidian") ? shaft : core;
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, wand.get()).define('X', shaft).define('G', core).pattern("XGX").pattern("XGX").pattern("XGX")
                .unlockedBy(unlock, has(unlockedBy)).save(this.output, ResourceKey.create(Registries.RECIPE, wand.getId()));
    }

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new WandsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
