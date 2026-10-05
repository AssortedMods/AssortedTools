package com.grim3212.assorted.portableworkbench.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class PortableWorkbenchRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public PortableWorkbenchRecipes(HolderLookup.Provider registries, RecipeOutput output) {
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

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, PortableWorkbenchItems.PORTABLE_WORKBENCH.get()).define('I', LibCommonTags.Items.INGOTS_IRON).define('W', Blocks.CRAFTING_TABLE)
                .pattern("III").pattern("IWI").pattern("III").unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE))
                .save(this.output, ResourceKey.create(Registries.RECIPE, PortableWorkbenchItems.PORTABLE_WORKBENCH.getId()));
    }

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new PortableWorkbenchRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
