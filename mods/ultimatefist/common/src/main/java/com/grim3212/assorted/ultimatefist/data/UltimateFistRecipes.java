package com.grim3212.assorted.ultimatefist.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class UltimateFistRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public UltimateFistRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
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

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new UltimateFistRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
