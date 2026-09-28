package com.grim3212.assorted.buckets.common.handlers;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.common.item.BetterBucketItem;
import com.grim3212.assorted.buckets.common.item.BetterMilkBucketItem;
import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.core.creative.CreativeTabItems;
import com.grim3212.assorted.lib.core.creative.SharedCreativeTabs;
import com.grim3212.assorted.lib.family.Families;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** This part's share of the Assorted Tools tab, which every part asks for and the first to load registers. */
public class BucketsCreativeItems {

    public static final ResourceKey<CreativeModeTab> TAB = Families.tab(Constants.FAMILY_ID);

    public static void init() {
        SharedCreativeTabs.add(TAB, 80, BucketsCreativeItems::items);
    }

    /** Each bucket, then its milk bucket already full. */
    private static List<ItemStack> items() {
        CreativeTabItems items = new CreativeTabItems();
        for (BucketPair pair : BucketsItems.pairs()) {
            items.addIfObtainable(pair.bucket().get(), pair.tier().getRepairItems());
            items.addIfObtainable(fullMilkBucket(pair.milk().get()), pair.tier().getRepairItems());
        }
        return items.getItems();
    }

    private static ItemStack fullMilkBucket(BetterMilkBucketItem milkBucket) {
        ItemStack stack = new ItemStack(milkBucket);
        BetterBucketItem.store(stack, "milk", milkBucket.getParent().getMaximumMillibuckets());
        return stack;
    }
}
