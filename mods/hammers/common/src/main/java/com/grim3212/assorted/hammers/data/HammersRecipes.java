package com.grim3212.assorted.hammers.data;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
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

import java.util.concurrent.CompletableFuture;

public class HammersRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public HammersRecipes(HolderLookup.Provider registries, RecipeOutput output) {
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

        hammerPattern(HammersItems.NETHERITE_HAMMER.get(), LibCommonTags.Items.INGOTS_NETHERITE);
        hammerPattern(HammersItems.DIAMOND_HAMMER.get(), LibCommonTags.Items.GEMS_DIAMOND);
        hammerPattern(HammersItems.IRON_HAMMER.get(), LibCommonTags.Items.INGOTS_IRON);
        hammerPattern(HammersItems.GOLD_HAMMER.get(), LibCommonTags.Items.INGOTS_GOLD);
        hammerPattern(HammersItems.STONE_HAMMER.get(), ItemTags.STONE_TOOL_MATERIALS);
        hammerPattern(HammersItems.WOOD_HAMMER.get(), ItemTags.PLANKS);

        ToolTiers.get().extras().forEach((name, tier) -> hammerPattern(HammersItems.EXTRA_HAMMERS.get(name).get(), tier.getRepairItems()));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(HammersItems.DIAMOND_HAMMER.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, HammersItems.NETHERITE_HAMMER.get())
                .unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(Identifier.parse(id(HammersItems.NETHERITE_HAMMER.get()) + "_smithing")));
    }

    private void hammerPattern(ItemLike output, TagKey<Item> input) {
        Identifier id = id(output.asItem());
        this.addConditions(itemTagExists(input), id);

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("III").pattern("ISI").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id));
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
            return new HammersRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
