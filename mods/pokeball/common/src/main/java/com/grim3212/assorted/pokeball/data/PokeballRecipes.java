package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class PokeballRecipes extends ConditionalRecipeProvider {

    public PokeballRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing here needs another mod's material.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PokeballItems.POKEBALL.get()).define('R', LibCommonTags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).define('I', LibCommonTags.Items.INGOTS_IRON).define('B', Items.STONE_BUTTON)
                .pattern("RRR").pattern("CBC").pattern("III").unlockedBy("has_iron", has(LibCommonTags.Items.INGOTS_IRON)).unlockedBy("has_redstone", has(LibCommonTags.Items.DUSTS_REDSTONE))
                .save(this.output, id(PokeballItems.POKEBALL.get()));
    }
}
