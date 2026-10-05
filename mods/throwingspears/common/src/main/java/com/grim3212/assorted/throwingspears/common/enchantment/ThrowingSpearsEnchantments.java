package com.grim3212.assorted.throwingspears.common.enchantment;

import com.grim3212.assorted.throwingspears.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** The keys the throwing spears look their enchantments up by. The enchantments themselves are data. */
public class ThrowingSpearsEnchantments {

    public static final ResourceKey<Enchantment> BOUNCINESS = key("bounciness");
    public static final ResourceKey<Enchantment> CONDUCTIVE = key("conductive");
    public static final ResourceKey<Enchantment> FLAMMABLE = key("flammable");
    public static final ResourceKey<Enchantment> UNSTABLE = key("unstable");

    /** What the four enchantments name as their supported items, in place of the old {@code canEnchant} checks. */
    public static final TagKey<Item> SPEAR_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "enchantable/throwing_spear"));

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static int getConductivity(ItemStack stack) {
        return getLevel(stack, CONDUCTIVE);
    }

    public static boolean hasFlammable(ItemStack stack) {
        return getLevel(stack, FLAMMABLE) > 0;
    }

    public static int getInstability(ItemStack stack) {
        return getLevel(stack, UNSTABLE);
    }

    public static int getMaxBounces(ItemStack stack) {
        return getLevel(stack, BOUNCINESS);
    }

    /** Read off the stack's own component: a {@code Holder<Enchantment>} cannot be resolved without a registry lookup. */
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
