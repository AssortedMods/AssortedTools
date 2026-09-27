package com.grim3212.assorted.multitools.mixin.item;

import com.grim3212.assorted.multitools.common.item.MultiToolItem;
import net.minecraft.world.item.ItemInstance;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;

/**
 * NeoForge's patched axe, shovel and hoe {@code useOn} return early unless {@code canPerformAction}
 * allows the ability; a mixin is needed since that method isn't on the vanilla jar.
 */
@Mixin(MultiToolItem.class)
public class MultiToolItemMixin {

    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility)
                // Sweeping is the only sword ability that exists, so it's named directly here.
                || ItemAbilities.SWORD_SWEEP == itemAbility;
    }
}
