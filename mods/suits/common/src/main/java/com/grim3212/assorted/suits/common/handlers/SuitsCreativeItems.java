package com.grim3212.assorted.suits.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.suits.Family;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class SuitsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 30, () -> SuitsItems.suits().stream().map(ItemStack::new).toList());
    }
}
