package com.grim3212.assorted.wands.common.handlers;

import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.common.item.WandBreakingItem.BreakingMode;
import com.grim3212.assorted.wands.common.item.WandBuildingItem.BuildingMode;
import com.grim3212.assorted.wands.common.item.WandMiningItem.MiningMode;
import com.grim3212.assorted.wands.common.item.WandsItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class WandsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 70, WandsCreativeItems::items);
    }

    // Each wand starts in its first mode, so the tab's copy shows one in its tooltip.
    private static List<ItemStack> items() {
        return List.of(
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()),
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()),
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()),
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.REINFORCED_BREAKING_WAND.get()), "Mode", BreakingMode.BREAK_WEAK.getSerializedName()),
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.REINFORCED_BUILDING_WAND.get()), "Mode", BuildingMode.BUILD_BOX.getSerializedName()),
                NBTHelper.putStringItemStack(new ItemStack(WandsItems.REINFORCED_MINING_WAND.get()), "Mode", MiningMode.MINE_ALL.getSerializedName()));
    }
}
