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
    public static boolean matches(ItemStack stack, Object item) {
        // Object, not Item: only `is(T rawType)` erases to `is(Object)`, which is what Mixin matches, so this is always an Item.
        Item asItem = (Item) item;

        if (asItem != Items.SHEARS) {
            return stack.is(asItem);
        }

        return stack.is(asItem) || stack.getItem() instanceof ShearsItem || stack.is(LibCommonTags.Items.SHEARS);
    }
}
