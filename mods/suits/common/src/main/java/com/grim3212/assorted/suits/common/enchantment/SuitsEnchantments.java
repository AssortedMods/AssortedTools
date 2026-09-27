package com.grim3212.assorted.suits.common.enchantment;

import com.grim3212.assorted.suits.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** The key the chicken jump enchantment is looked up by. The enchantment itself is data, written by {@code SuitsEnchantmentData}. */
public class SuitsEnchantments {

    public static final ResourceKey<Enchantment> CHICKEN_JUMP = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "chicken_jump"));

    public static boolean hasChickenJump(ItemStack stack) {
        return getLevel(stack, CHICKEN_JUMP) > 0;
    }

    /** Read off the stack's own component, since a {@code Holder<Enchantment>} needs a registry lookup a static caller lacks. */
    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(enchantment)) {
                return enchantments.getLevel(holder);
            }
        }

        return 0;
    }
}
