package com.grim3212.assorted.shears.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ShearsRecipes extends ConditionalRecipeProvider {

    public ShearsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
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

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ShearsItems.DIAMOND_SHEARS.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.TOOLS, ShearsItems.NETHERITE_SHEARS.get())
                .unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, ResourceLocation.parse(id(ShearsItems.NETHERITE_SHEARS.get()) + "_smithing"));
    }

    /** Both hands: the pattern and its mirror, as the old recipes were. */
    private void shearPattern(ItemLike output, TagKey<Item> input) {
        ResourceLocation id = id(output.asItem());
        ResourceLocation alt = ResourceLocation.parse(id + "_alt");
        this.addConditions(itemTagExists(input), id, alt);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern(" I").pattern("IL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, id);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output).define('I', input).define('L', LibCommonTags.Items.LEATHER).pattern("LI").pattern("I ").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).unlockedBy("has_item", has(input)).save(this.output, alt);
    }
}
