package com.grim3212.assorted.buckets.data;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BucketsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public BucketsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output, Constants.MOD_ID);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void registerConditions() {
        // Each pattern adds its own material condition as it builds.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        bucketPattern(BucketsItems.WOOD.bucket().get(), ItemTags.PLANKS);
        bucketPattern(BucketsItems.STONE.bucket().get(), ItemTags.STONE_TOOL_MATERIALS);
        bucketPattern(BucketsItems.GOLD.bucket().get(), LibCommonTags.Items.INGOTS_GOLD);
        bucketPattern(BucketsItems.DIAMOND.bucket().get(), LibCommonTags.Items.GEMS_DIAMOND);
        bucketPattern(BucketsItems.NETHERITE.bucket().get(), LibCommonTags.Items.INGOTS_NETHERITE);
        BucketsItems.EXTRAS.forEach(pair -> bucketPattern(pair.bucket().get(), pair.tier().getRepairItems()));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(BucketsItems.DIAMOND.bucket().get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, BucketsItems.NETHERITE.bucket().get())
                .unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(Identifier.parse(id(BucketsItems.NETHERITE.bucket().get()) + "_smithing")));

        // Vanilla's cake asks for its own milk bucket by identity, so this one takes any of them.
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.FOOD, Blocks.CAKE).define('A', LibCommonTags.Items.BUCKETS_MILK).define('B', Items.SUGAR).define('C', LibCommonTags.Items.CROPS_WHEAT).define('E', LibCommonTags.Items.EGGS)
                .pattern("AAA").pattern("BEB").pattern("CCC").unlockedBy("has_egg", has(LibCommonTags.Items.EGGS)).save(this.output, recipeKey(prefix("cake_alt")));
    }

    private void bucketPattern(ItemLike output, TagKey<Item> input) {
        Identifier id = id(output.asItem());
        this.addConditions(itemTagExists(input), id);

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).pattern("I I").pattern(" I ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id));
    }

    private static ResourceKey<Recipe<?>> recipeKey(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }

    /** What the loader datagen entry points register; it builds a fresh provider around each output. */
    public static class Runner extends ConditionalRecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, Constants.MOD_ID);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new BucketsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
