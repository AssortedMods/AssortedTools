package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

/**
 * NeoForge asks the held item whether it can strip, path or till before any block changes, so the multitool answers
 * for every tool it stands in for, not only the sword it is built on.
 */
public class NeoForgeMultiToolItem extends MultiToolItem {

    public NeoForgeMultiToolItem(ToolTier tier, float durabilityModifier, Item.Properties properties) {
        super(tier, durabilityModifier, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility);
    }

    /** Named in {@code META-INF/services}, so the common items make this subclass on NeoForge. */
    public static final class Factory implements MultiToolFactory {

        @Override
        public MultiToolItem create(ToolTier tier, float durabilityModifier, Item.Properties properties) {
            return new NeoForgeMultiToolItem(tier, durabilityModifier, properties);
        }
    }
}
