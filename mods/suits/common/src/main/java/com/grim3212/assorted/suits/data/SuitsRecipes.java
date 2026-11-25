package com.grim3212.assorted.suits.data;

import com.grim3212.assorted.lib.core.conditions.ConditionalRecipeProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SuitsRecipes extends ConditionalRecipeProvider {

    public SuitsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Constants.MOD_ID);
    }

    @Override
    public void registerConditions() {
        // Each suit adds its own material condition as it builds.
    }

    @Override
    public void buildRecipes() {
        super.buildRecipes();

        armorSet(SuitsItems.CHICKEN_SUIT_HELMET.get(), SuitsItems.CHICKEN_SUIT_CHESTPLATE.get(), SuitsItems.CHICKEN_SUIT_LEGGINGS.get(), SuitsItems.CHICKEN_SUIT_BOOTS.get(), LibCommonTags.Items.FEATHERS);
        scubaSuit();
        lavaSuit();
    }

    /**
     * The scuba suit: a leather wetsuit with a glass visor in the mask and copper fittings on the
     * tank, legs and fins. {@link #armorSet} cannot build it, working as it does from one material.
     */
    private void scubaSuit() {
        ItemLike helmet = SuitsItems.SCUBA_HELMET.get();
        ItemLike chestplate = SuitsItems.SCUBA_CHESTPLATE.get();
        ItemLike leggings = SuitsItems.SCUBA_LEGGINGS.get();
        ItemLike boots = SuitsItems.SCUBA_BOOTS.get();

        this.addConditions(itemTagExists(LibCommonTags.Items.INGOTS_COPPER),
                key(helmet.asItem()), key(chestplate.asItem()), key(leggings.asItem()), key(boots.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('L', LibCommonTags.Items.LEATHER).define('G', LibCommonTags.Items.GLASS)
                .pattern("LLL").pattern("LGL").unlockedBy("has_leather", has(LibCommonTags.Items.LEATHER)).save(this.output, key(helmet.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('L', LibCommonTags.Items.LEATHER).define('C', LibCommonTags.Items.INGOTS_COPPER)
                .pattern("L L").pattern("LCL").pattern("LLL").unlockedBy("has_copper", has(LibCommonTags.Items.INGOTS_COPPER)).save(this.output, key(chestplate.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('L', LibCommonTags.Items.LEATHER).define('C', LibCommonTags.Items.INGOTS_COPPER)
                .pattern("LCL").pattern("L L").pattern("L L").unlockedBy("has_copper", has(LibCommonTags.Items.INGOTS_COPPER)).save(this.output, key(leggings.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('L', LibCommonTags.Items.LEATHER).define('C', LibCommonTags.Items.INGOTS_COPPER)
                .pattern("L L").pattern("LCL").unlockedBy("has_copper", has(LibCommonTags.Items.INGOTS_COPPER)).save(this.output, key(boots.asItem()));
    }

    /**
     * The lava suit: magma cream, blaze rod fittings, a crying obsidian visor and netherite in the two pieces that take
     * the worst of it. All of it comes out of the nether, the only place it is any use.
     */
    private void lavaSuit() {
        ItemLike helmet = SuitsItems.LAVA_HELMET.get();
        ItemLike chestplate = SuitsItems.LAVA_CHESTPLATE.get();
        ItemLike leggings = SuitsItems.LAVA_LEGGINGS.get();
        ItemLike boots = SuitsItems.LAVA_BOOTS.get();

        this.addConditions(itemTagExists(LibCommonTags.Items.INGOTS_NETHERITE),
                key(helmet.asItem()), key(chestplate.asItem()), key(leggings.asItem()), key(boots.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('M', Items.MAGMA_CREAM).define('O', Items.CRYING_OBSIDIAN)
                .pattern("MMM").pattern("MOM").unlockedBy("has_magma_cream", has(Items.MAGMA_CREAM)).save(this.output, key(helmet.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('M', Items.MAGMA_CREAM).define('N', LibCommonTags.Items.INGOTS_NETHERITE).define('B', LibCommonTags.Items.RODS_BLAZE)
                .pattern("M M").pattern("MNM").pattern("MBM").unlockedBy("has_netherite", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, key(chestplate.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('M', Items.MAGMA_CREAM).define('N', LibCommonTags.Items.INGOTS_NETHERITE).define('B', LibCommonTags.Items.RODS_BLAZE)
                .pattern("MNM").pattern("M M").pattern("B B").unlockedBy("has_netherite", has(LibCommonTags.Items.INGOTS_NETHERITE)).save(this.output, key(leggings.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('M', Items.MAGMA_CREAM).define('B', LibCommonTags.Items.RODS_BLAZE)
                .pattern("M M").pattern("MBM").unlockedBy("has_magma_cream", has(Items.MAGMA_CREAM)).save(this.output, key(boots.asItem()));
    }

    private void armorSet(ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, TagKey<Item> input) {
        this.addConditions(itemTagExists(input), key(helmet.asItem()), key(chestplate.asItem()), key(leggings.asItem()), key(boots.asItem()));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet).define('X', input).pattern("XXX").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, key(helmet.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate).define('X', input).pattern("X X").pattern("XXX").pattern("XXX").unlockedBy("has_item", has(input)).save(this.output, key(chestplate.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings).define('X', input).pattern("XXX").pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, key(leggings.asItem()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots).define('X', input).pattern("X X").pattern("X X").unlockedBy("has_item", has(input)).save(this.output, key(boots.asItem()));
    }

    private ResourceLocation key(Item item) {
        return id(item);
    }
}
