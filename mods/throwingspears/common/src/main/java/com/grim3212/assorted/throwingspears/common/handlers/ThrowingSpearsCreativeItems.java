package com.grim3212.assorted.throwingspears.common.handlers;

import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class ThrowingSpearsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 90, ThrowingSpearsCreativeItems::items);
    }

    private static List<ItemStack> items() {
        CreativeTabItems items = new CreativeTabItems();
        ThrowingSpearsItems.spears().forEach(spear -> items.addIfObtainable(spear, spear.getToolTier().getRepairItems()));
        return items.getItems();
    }
}
