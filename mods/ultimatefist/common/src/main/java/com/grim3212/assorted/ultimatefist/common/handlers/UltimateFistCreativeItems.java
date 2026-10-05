package com.grim3212.assorted.ultimatefist.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class UltimateFistCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 20, UltimateFistCreativeItems::items);
    }

    // The fist, then its fragments in the order the tab always listed them.
    private static List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(UltimateFistItems.ULTIMATE_FIST.get()));
        List.of(UltimateFistItems.A_FRAGMENT, UltimateFistItems.E_FRAGMENT, UltimateFistItems.I_FRAGMENT, UltimateFistItems.L_FRAGMENT,
                UltimateFistItems.M_FRAGMENT, UltimateFistItems.T_FRAGMENT, UltimateFistItems.U_FRAGMENT, UltimateFistItems.MISSING_FRAGMENT)
                .forEach(fragment -> items.add(new ItemStack(fragment.get())));
        return items;
    }
}
