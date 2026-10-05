package com.grim3212.assorted.boomerangs.common.handlers;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class BoomerangsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 60, () -> List.of(new ItemStack(BoomerangsItems.WOOD_BOOMERANG.get()), new ItemStack(BoomerangsItems.DIAMOND_BOOMERANG.get())));
    }
}
