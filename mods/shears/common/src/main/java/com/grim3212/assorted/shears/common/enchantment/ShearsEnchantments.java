package com.grim3212.assorted.shears.common.enchantment;

import com.grim3212.assorted.shears.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** The key Coral Cutter is looked up by. The enchantment itself is data, written by {@code ShearsEnchantmentData}. */
public final class ShearsEnchantments {

    public static final ResourceKey<Enchantment> CORAL_CUTTER = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "coral_cutter"));

    private ShearsEnchantments() {
    }

    /** Read off the stack's own component, since a {@code Holder} needs a registry lookup this has no access to. */
    public static boolean hasCoralCutter(ItemStack stack) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(CORAL_CUTTER)) {
                return enchantments.getLevel(holder) > 0;
            }
        }
        return false;
    }
}
