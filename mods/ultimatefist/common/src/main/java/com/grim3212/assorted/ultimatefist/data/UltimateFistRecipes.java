package com.grim3212.assorted.ultimatefist.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class UltimateFistRecipes extends ConditionalRecipeProvider {

    public UltimateFistRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Nothing here needs another mod's material.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, UltimateFistItems.ULTIMATE_FIST.get()).requires(UltimateFistItems.U_FRAGMENT.get()).requires(UltimateFistItems.L_FRAGMENT.get())
                .requires(UltimateFistItems.T_FRAGMENT.get()).requires(UltimateFistItems.I_FRAGMENT.get()).requires(UltimateFistItems.M_FRAGMENT.get()).requires(UltimateFistItems.A_FRAGMENT.get())
                .requires(UltimateFistItems.MISSING_FRAGMENT.get()).requires(UltimateFistItems.E_FRAGMENT.get()).requires(LibCommonTags.Items.NETHER_STARS)
                .unlockedBy("has_nether_star", has(LibCommonTags.Items.NETHER_STARS)).unlockedBy("has_fragment", has(UltimateFistItemTagProvider.ULTIMATE_FRAGMENTS))
                .save(this.output, id(UltimateFistItems.ULTIMATE_FIST.get()));
    }
}
