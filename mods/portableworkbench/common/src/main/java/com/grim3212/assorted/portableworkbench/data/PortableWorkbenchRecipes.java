package com.grim3212.assorted.portableworkbench.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

public class PortableWorkbenchRecipes extends ConditionalRecipeProvider {

    public PortableWorkbenchRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
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
}
