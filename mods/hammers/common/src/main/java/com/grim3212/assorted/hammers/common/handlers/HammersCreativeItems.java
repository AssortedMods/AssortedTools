package com.grim3212.assorted.hammers.common.handlers;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class HammersCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 100, HammersCreativeItems::items);
    }

    private static List<ItemStack> items() {
        CreativeTabItems items = new CreativeTabItems();
        HammersItems.hammers().forEach(hammer -> items.addIfObtainable(hammer, hammer.getToolTier().getRepairItems()));
        return items.getItems();
    }
}
