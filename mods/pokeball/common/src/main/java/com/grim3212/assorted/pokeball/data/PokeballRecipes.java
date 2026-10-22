package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class PokeballRecipes extends ConditionalRecipeProvider {

    public PokeballRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
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
}
