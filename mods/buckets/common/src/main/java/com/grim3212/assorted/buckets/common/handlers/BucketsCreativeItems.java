package com.grim3212.assorted.buckets.common.handlers;

import com.grim3212.assorted.buckets.Family;
import com.grim3212.assorted.buckets.common.item.BetterBucketItem;
import com.grim3212.assorted.buckets.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class BucketsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = SharedCreativeTabs.tab(Identifier.fromNamespaceAndPath(Family.ID, "tab"), Family.ICONS);

    public static void init() {
        SharedCreativeTabs.add(TAB, 80, BucketsCreativeItems::items);
    }

    /** Each bucket, then its milk bucket already full. */
    private static List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();
        for (BucketPair pair : BucketsItems.pairs()) {
            if (ToolTiers.get().shown(pair.tier())) {
                items.add(new ItemStack(pair.bucket().get()));
                items.add(fullMilkBucket(pair.milk().get()));
            }
        }
        return items;
    }

    private static ItemStack fullMilkBucket(BetterMilkBucketItem milkBucket) {
        ItemStack stack = new ItemStack(milkBucket);
        BetterBucketItem.store(stack, "milk", milkBucket.getParent().getMaximumMillibuckets());
        return stack;
    }
}
