package com.grim3212.assorted.boomerangs.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Recipe;

public class BoomerangsRecipes extends ConditionalRecipeProvider {

    public BoomerangsRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
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
}
