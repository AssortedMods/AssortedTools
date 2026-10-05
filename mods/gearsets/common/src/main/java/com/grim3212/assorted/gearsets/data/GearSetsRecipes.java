package com.grim3212.assorted.gearsets.data;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
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
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class GearSetsRecipes extends ConditionalRecipeProvider {

    private final HolderGetter<Item> items;

    public GearSetsRecipes(HolderLookup.Provider registries, RecipeOutput output) {
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

        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            toolSet(set.pickaxe().get(), set.shovel().get(), set.axe().get(), set.hoe().get(), set.sword().get(), set.material());
            armorSet(set.helmet().get(), set.chestplate().get(), set.leggings().get(), set.boots().get(), set.material());
            lungeSpearPattern(set.spear().get(), set.material());
        }
    }

    /** Vanilla's own spear recipe, for a material vanilla has no spear of. */
    private void lungeSpearPattern(ItemLike output, TagKey<Item> input) {
        this.addConditions(itemTagExists(input), id(output.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, output).define('S', LibCommonTags.Items.RODS_WOODEN).define('I', input).pattern("  I").pattern(" S ").pattern("S  ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(output.asItem())));
    }

    private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input) {
        Identifier axeAlt = alt(axe);
        Identifier hoeAlt = alt(hoe);
        this.addConditions(itemTagExists(input), id(pickaxe.asItem()), id(shovel.asItem()), id(axe.asItem()), axeAlt, id(hoe.asItem()), hoeAlt, id(sword.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, pickaxe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(pickaxe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, shovel).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(shovel.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(axe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(axeAlt));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(hoe.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, recipeKey(hoeAlt));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, sword).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(sword.asItem())));
    }

    private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input) {
        this.addConditions(itemTagExists(input), id(helmet.asItem()), id(chestplate.asItem()), id(leggings.asItem()), id(boots.asItem()));

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(helmet.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(chestplate.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(leggings.asItem())));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.COMBAT, boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, recipeKey(id(boots.asItem())));
    }

    private Identifier alt(ItemLike item) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, id(item.asItem()).getPath() + "_alt");
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
            return new GearSetsRecipes(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes: " + Constants.MOD_ID;
        }
    }
}
