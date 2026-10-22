package com.grim3212.assorted.shears.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class ShearsRecipes extends ConditionalRecipeProvider {

    public ShearsRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Each pattern adds its own material condition as it builds.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        shearPattern(ShearsItems.WOOD_SHEARS.get(), ItemTags.PLANKS);
        shearPattern(ShearsItems.STONE_SHEARS.get(), ItemTags.STONE_TOOL_MATERIALS);
        shearPattern(ShearsItems.GOLD_SHEARS.get(), LibCommonTags.Items.INGOTS_GOLD);
        shearPattern(ShearsItems.DIAMOND_SHEARS.get(), LibCommonTags.Items.GEMS_DIAMOND);
        shearPattern(ShearsItems.NETHERITE_SHEARS.get(), LibCommonTags.Items.INGOTS_NETHERITE);

        ToolTiers.get().extras().forEach((name, tier) -> shearPattern(ShearsItems.EXTRA_SHEARS.get(name).get(), tier.getRepairItems()));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ShearsItems.DIAMOND_SHEARS.get()), this.tag(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ShearsItems.NETHERITE_SHEARS.get())
                .unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, recipeKey(Identifier.parse(id(ShearsItems.NETHERITE_SHEARS.get()) + "_smithing")));
    }

    /** Both hands: the pattern and its mirror, as the old recipes were. */
    private void shearPattern(ItemLike output, TagKey<Item> input) {
        Identifier id = id(output.asItem());
        Identifier alt = Identifier.parse(id + "_alt");
        this.addConditions(itemTagExists(input), id, alt);

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(id));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, recipeKey(alt));
    }

    private static ResourceKey<Recipe<?>> recipeKey(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }
}
