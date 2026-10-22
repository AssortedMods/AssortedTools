package com.grim3212.assorted.multitools.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.core.conditions.LibConditionProvider;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.api.MultitoolsTags;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class MultitoolsRecipes extends ConditionalRecipeProvider {

    public MultitoolsRecipes(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Each pattern adds its own material condition as it builds.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        multiTool(MultitoolsItems.WOODEN_MULTITOOL.get(), ItemTags.PLANKS, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD);
        multiTool(MultitoolsItems.STONE_MULTITOOL.get(), ItemTags.STONE_TOOL_MATERIALS, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD);
        multiTool(MultitoolsItems.GOLDEN_MULTITOOL.get(), LibCommonTags.Items.INGOTS_GOLD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD);
        multiTool(MultitoolsItems.IRON_MULTITOOL.get(), LibCommonTags.Items.INGOTS_IRON, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD);
        multiTool(MultitoolsItems.DIAMOND_MULTITOOL.get(), LibCommonTags.Items.GEMS_DIAMOND, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SWORD);
        multiTool(MultitoolsItems.NETHERITE_MULTITOOL.get(), LibCommonTags.Items.INGOTS_NETHERITE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SWORD);

        ToolTiers.get().extras().forEach((name, tier) -> extraMultiTool(MultitoolsItems.EXTRA_MULTITOOLS.get(name).get(), name, tier.getRepairItems()));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(MultitoolsItems.DIAMOND_MULTITOOL.get()), Ingredient.of(Blocks.NETHERITE_BLOCK), RecipeCategory.TOOLS, MultitoolsItems.NETHERITE_MULTITOOL.get())
                .unlocks("has_netherite_block", has(Blocks.NETHERITE_BLOCK)).save(this.output, recipeKey(Identifier.parse(id(MultitoolsItems.NETHERITE_MULTITOOL.get()) + "_smithing")));
    }

    private void multiTool(ItemLike output, TagKey<Item> input, ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword) {
        Identifier id = id(output.asItem());
        this.addConditions(itemTagExists(input), id);

        ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.TOOLS, output).requires(pickaxe).requires(shovel).requires(axe).requires(hoe).requires(sword)
                .requires(this.tag(input)).requires(this.tag(input)).requires(this.tag(input)).requires(this.tag(input))
                .unlockedBy("has_item", has(input)).save(this.output, recipeKey(id));
    }

    /**
     * The five tools come from their {@code c:<kind>/<material>} tags rather than items, so this mod does not need
     * Assorted Gear Sets; the recipe only loads once some mod fills all five and the material itself.
     */
    private void extraMultiTool(ItemLike output, String material, TagKey<Item> input) {
        Identifier id = id(output.asItem());
        List<LibConditionProvider> conditions = new ArrayList<>(List.of(itemTagExists(input)));
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.TOOLS, output);
        for (String kind : MultitoolsTags.RECIPE_TOOL_KINDS) {
            TagKey<Item> tools = MultitoolsTags.materialTools(kind, material);
            conditions.add(itemTagExists(tools));
            builder.requires(this.tag(tools));
        }
        this.addConditions(and(conditions.toArray(LibConditionProvider[]::new)), id);

        builder.requires(this.tag(input)).requires(this.tag(input)).requires(this.tag(input)).requires(this.tag(input))
                .unlockedBy("has_item", has(input)).save(this.output, recipeKey(id));
    }

    private static ResourceKey<Recipe<?>> recipeKey(Identifier id) {
        return ResourceKey.create(Registries.RECIPE, id);
    }
}
