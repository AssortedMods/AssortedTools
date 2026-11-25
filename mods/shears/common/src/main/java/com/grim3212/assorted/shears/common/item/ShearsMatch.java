package com.grim3212.assorted.shears.common.item;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;

/**
 * Vanilla checks {@code is(Items.SHEARS)} by identity, but NeoForge patches those call sites to
 * {@code canPerformAction(ItemAbilities.SHEARS_*)} and Fabric's mixins redirect them here instead.
 */
public final class ShearsMatch {

    private ShearsMatch() {
    }

    /**
     * Whether {@code stack} should satisfy a vanilla {@code is(item)} check. Only widens a check
     * for shears, since the redirected call sites test other items too.
     */
    public static boolean matches(ItemStack stack, Item item) {
        if (item != Items.SHEARS) {
            return stack.is(item);
        }

        return isShears(stack);
    }

    /** Vanilla shears, any {@link ShearsItem}, or anything in the common shears tag. */
    public static boolean isShears(ItemStack stack) {
        return stack.is(Items.SHEARS) || stack.getItem() instanceof ShearsItem || stack.is(LibCommonTags.Items.SHEARS);
    }
}
