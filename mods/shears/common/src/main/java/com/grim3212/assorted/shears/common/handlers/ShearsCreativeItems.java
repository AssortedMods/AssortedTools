package com.grim3212.assorted.shears.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.shears.Family;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class ShearsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 130, ShearsCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return ShearsItems.shears().stream().filter(shears -> ToolTiers.get().shown(shears.getToolTier())).map(ItemStack::new).toList();
    }
}
