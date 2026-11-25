package com.grim3212.assorted.machetes.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.machetes.Constants;
import com.grim3212.assorted.machetes.common.item.MachetesItems;
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

public class MachetesRecipes extends ConditionalRecipeProvider {

    public MachetesRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Each pattern adds its own material condition as it builds.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        machetePattern(MachetesItems.WOOD_MACHETE.get(), ItemTags.PLANKS);
        machetePattern(MachetesItems.STONE_MACHETE.get(), ItemTags.STONE_TOOL_MATERIALS);
        machetePattern(MachetesItems.GOLD_MACHETE.get(), LibCommonTags.Items.INGOTS_GOLD);
        machetePattern(MachetesItems.IRON_MACHETE.get(), LibCommonTags.Items.INGOTS_IRON);
        machetePattern(MachetesItems.DIAMOND_MACHETE.get(), LibCommonTags.Items.GEMS_DIAMOND);

        ToolTiers.get().extras().forEach((name, tier) -> machetePattern(MachetesItems.EXTRA_MACHETES.get(name).get(), tier.getRepairItems()));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(MachetesItems.DIAMOND_MACHETE.get()), Ingredient.of(LibCommonTags.Items.INGOTS_NETHERITE), RecipeCategory.COMBAT, MachetesItems.NETHERITE_MACHETE.get())
                .unlocks("has_netherite_ingot", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, ResourceLocation.parse(id(MachetesItems.NETHERITE_MACHETE.get()) + "_smithing"));
    }

    /** A blade two wide on the diagonal, above a handle. */
    private void machetePattern(ItemLike output, TagKey<Item> input) {
        ResourceLocation id = id(output.asItem());
        this.addConditions(itemTagExists(input), id);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('X', input).pattern(" XX").pattern("XX ").pattern("S  ").unlockedBy("has_item", has(input)).save(this.output, id);
    }
}
