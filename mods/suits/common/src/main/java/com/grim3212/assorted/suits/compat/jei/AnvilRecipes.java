package com.grim3212.assorted.suits.compat.jei;

import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import com.grim3212.assorted.suits.common.item.ChickenSuitArmor;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AnvilRecipes {

    public static Map<TagKey<Item>, Supplier<ChickenSuitArmor>> CHICKEN_JUMP_MAP = Map.ofEntries(
            Map.entry(ItemTags.HEAD_ARMOR, SuitsItems.CHICKEN_SUIT_HELMET),
            Map.entry(ItemTags.CHEST_ARMOR, SuitsItems.CHICKEN_SUIT_CHESTPLATE),
            Map.entry(ItemTags.LEG_ARMOR, SuitsItems.CHICKEN_SUIT_LEGGINGS),
            Map.entry(ItemTags.FOOT_ARMOR, SuitsItems.CHICKEN_SUIT_BOOTS)
    );

    /**
     * Takes a {@link HolderLookup.Provider} because writing an enchantment onto a stack needs its
     * {@link Holder}; JEI's context supplies it.
     */
    public static List<IJeiAnvilRecipe> chickenEnchantRecipes(IVanillaRecipeFactory recipeFactory, IIngredientManager ingredientManager, HolderLookup.Provider registries) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();

        // A datapack may have removed it.
        Optional<Holder.Reference<Enchantment>> chickenJumpHolder = registries.lookupOrThrow(Registries.ENCHANTMENT).get(SuitsEnchantments.CHICKEN_JUMP);
        if (chickenJumpHolder.isEmpty()) {
            return recipes;
        }
        Holder<Enchantment> chickenJump = chickenJumpHolder.get();

        CHICKEN_JUMP_MAP.forEach((tag, item) -> {
            var armors = ingredientManager.getAllItemStacks()
                    .stream()
                    .filter(i -> i.isEnchantable() && i.is(tag) && !(i.getItem() instanceof ChickenSuitArmor)).toList();

            var enchantedArmors = armors.stream().map((stack) -> getChickenEnchanted(stack, chickenJump)).toList();

            // Keyed by the armor slot's tag, since that's what makes these four recipes distinct.
            Identifier uid = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chicken_jump/" + tag.location().getNamespace() + "/" + tag.location().getPath().replace('/', '_'));

            recipes.add(recipeFactory.createAnvilRecipe(armors, List.of(new ItemStack(item.get())), enchantedArmors, uid));
        });

        return recipes;
    }

    /**
     * Enchantments are the {@code minecraft:enchantments} data component, an {@link ItemEnchantments},
     * with the stack passed first.
     */
    private static ItemStack getChickenEnchanted(ItemStack ingredient, Holder<Enchantment> chickenJump) {
        ItemStack enchantedIngredient = ingredient.copy();

        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(chickenJump, 1);
        EnchantmentHelper.setEnchantments(enchantedIngredient, enchantments.toImmutable());

        return enchantedIngredient;
    }
}
