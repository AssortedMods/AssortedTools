package com.grim3212.assorted.ultimatefist.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public class UltimateFistRecipes extends ConditionalRecipeProvider {

    public UltimateFistRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing here needs another mod's material.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.COMBAT, UltimateFistItems.ULTIMATE_FIST.get()).requires(UltimateFistItems.U_FRAGMENT.get()).requires(UltimateFistItems.L_FRAGMENT.get())
                .requires(UltimateFistItems.T_FRAGMENT.get()).requires(UltimateFistItems.I_FRAGMENT.get()).requires(UltimateFistItems.M_FRAGMENT.get()).requires(UltimateFistItems.A_FRAGMENT.get())
                .requires(UltimateFistItems.MISSING_FRAGMENT.get()).requires(UltimateFistItems.E_FRAGMENT.get()).requires(LibCommonTags.Items.NETHER_STARS)
                .unlockedBy("has_nether_star", has(LibCommonTags.Items.NETHER_STARS)).unlockedBy("has_fragment", has(UltimateFistItemTagProvider.ULTIMATE_FRAGMENTS))
                .save(this.output, ResourceKey.create(Registries.RECIPE, id(UltimateFistItems.ULTIMATE_FIST.get())));
    }
}
