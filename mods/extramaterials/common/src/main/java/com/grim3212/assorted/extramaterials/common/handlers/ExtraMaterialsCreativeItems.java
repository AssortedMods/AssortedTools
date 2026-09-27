package com.grim3212.assorted.extramaterials.common.handlers;

import com.grim3212.assorted.extramaterials.Family;
import com.grim3212.assorted.extramaterials.common.item.ExtraMaterialsItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class ExtraMaterialsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 140, ExtraMaterialsCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return ExtraMaterialsItems.MATERIALS.values().stream()
                .filter(set -> ToolTiers.get().shown(set.tier()))
                .flatMap(set -> set.items().stream())
                .map(ItemStack::new)
                .toList();
    }
}
