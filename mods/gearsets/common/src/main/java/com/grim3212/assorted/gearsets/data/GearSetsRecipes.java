package com.grim3212.assorted.gearsets.data;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class GearSetsRecipes extends ConditionalRecipeProvider {

    public GearSetsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
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
        }
    }

    private void toolSet(ItemLike pickaxe, ItemLike shovel, ItemLike axe, ItemLike hoe, ItemLike sword, TagKey<Item> input) {
        ResourceLocation axeAlt = alt(axe);
        ResourceLocation hoeAlt = alt(hoe);
        this.addConditions(itemTagExists(input), id(pickaxe.asItem()), id(shovel.asItem()), id(axe.asItem()), axeAlt, id(hoe.asItem()), hoeAlt, id(sword.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XXX").pattern(" S ").pattern(" S ").unlockedBy("has_item", has(input)).save(this.output, id(pickaxe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("S").pattern("S").unlockedBy("has_item", has(input)).save(this.output, id(shovel.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("XS").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, id(axe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("SX").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, axeAlt);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern(" S").pattern(" S").unlockedBy("has_item", has(input)).save(this.output, id(hoe.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("XX").pattern("S ").pattern("S ").unlockedBy("has_item", has(input)).save(this.output, hoeAlt);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword).define('X', input).define('S', LibCommonTags.Items.RODS_WOODEN).pattern("X").pattern("X").pattern("S").unlockedBy("has_item", has(input)).save(this.output, id(sword.asItem()));
    }

    private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input) {
        this.addConditions(itemTagExists(input), id(helmet.asItem()), id(chestplate.asItem()), id(leggings.asItem()), id(boots.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, id(helmet.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input)).save(this.output, id(chestplate.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, id(leggings.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, id(boots.asItem()));
    }

    private ResourceLocation alt(ItemLike item) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id(item.asItem()).getPath() + "_alt");
    }
}
