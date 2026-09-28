package com.grim3212.assorted.gearsets.common.handlers;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class GearSetsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 140, GearSetsCreativeItems::items);
    }

    private static List<ItemStack> items() {
        return GearSetsItems.MATERIALS.values().stream()
                .filter(set -> ToolTiers.get().shown(set.tier()))
                .flatMap(set -> set.items().stream())
                .map(ItemStack::new)
                .toList();
    }
}
