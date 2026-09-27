package com.grim3212.assorted.staffs.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.staffs.Family;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class StaffsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 50, () -> List.of(new ItemStack(StaffsItems.NEPTUNE_STAFF.get()), new ItemStack(StaffsItems.PHOENIX_STAFF.get()),
                new ItemStack(StaffsItems.FROST_ROD.get()), new ItemStack(StaffsItems.FROST_POWDER.get()), new ItemStack(StaffsItems.ICE_CHARGE.get()),
                new ItemStack(StaffsItems.POWER_STAFF.get())));
    }
}
