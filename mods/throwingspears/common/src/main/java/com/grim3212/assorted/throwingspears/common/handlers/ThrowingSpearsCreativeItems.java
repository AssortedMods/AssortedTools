package com.grim3212.assorted.throwingspears.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.throwingspears.Family;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class ThrowingSpearsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 90, ThrowingSpearsCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return ThrowingSpearsItems.spears().stream().filter(spear -> ToolTiers.get().shown(spear.getToolTier())).map(ItemStack::new).toList();
    }
}
