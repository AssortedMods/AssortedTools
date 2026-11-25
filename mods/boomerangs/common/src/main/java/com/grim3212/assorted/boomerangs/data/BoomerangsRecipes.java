package com.grim3212.assorted.boomerangs.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BoomerangsRecipes extends ConditionalRecipeProvider {

    public BoomerangsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Both are made of vanilla materials, so neither needs a condition.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BoomerangsItems.WOOD_BOOMERANG.get()).define('X', ItemTags.PLANKS).pattern("XX").pattern("X ").pattern("XX")
                .unlockedBy("has_planks", has(ItemTags.PLANKS)).save(this.output, BoomerangsItems.WOOD_BOOMERANG.getId());
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BoomerangsItems.DIAMOND_BOOMERANG.get()).define('X', LibCommonTags.Items.GEMS_DIAMOND).define('Y', BoomerangsItems.WOOD_BOOMERANG.get()).pattern("XX").pattern("XY").pattern("XX")
                .unlockedBy("has_diamonds", has(LibCommonTags.Items.GEMS_DIAMOND)).save(this.output, BoomerangsItems.DIAMOND_BOOMERANG.getId());
    }
}
