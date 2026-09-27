package com.grim3212.assorted.buckets.gametest;

import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.buckets.common.item.BetterBucketItem;
import com.grim3212.assorted.buckets.common.item.BucketsDataComponents;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/** A bucket's tooltip counts the whole buckets it holds against its capacity. */
final class BucketTooltipTests {

    private BucketTooltipTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("bucket_tooltip_counts_whole_buckets", BucketTooltipTests::bucketTooltipCountsWholeBuckets);
    }

    private static void bucketTooltipCountsWholeBuckets(GameTestHelper helper) {
        int oneBucket = BetterBucketItem.getBucketAmount();

        BetterBucketItem diamond = BucketsItems.DIAMOND.bucket().get();
        helper.assertValueEqual(tooltipKeys(helper, diamond.getEmptyStack(), BucketsDataComponents.BUCKET_CONTENTS.get()), List.of("tooltip.buckets.empty"), "an empty bucket's tooltip");

        ItemStack water = diamond.getEmptyStack();
        BetterBucketItem.storeFluid(water, Fluids.WATER, 2 * oneBucket);
        assertContains(helper, water, 2, diamond.getMaximumMillibuckets() / oneBucket, "a diamond bucket of water");

        ItemStack milk = new ItemStack(BucketsItems.WOOD.milk().get());
        BetterBucketItem.setAmount(milk, oneBucket);
        assertContains(helper, milk, 1, BucketsItems.WOOD.bucket().get().getMaximumMillibuckets() / oneBucket, "a wood milk bucket");

        helper.succeed();
    }

    private static void assertContains(GameTestHelper helper, ItemStack stack, int buckets, int capacity, String what) {
        List<Component> lines = tooltipLines(helper, stack, BucketsDataComponents.BUCKET_CONTENTS.get());
        helper.assertValueEqual(lines.stream().map(TestSupport::tooltipKey).toList(), List.of("tooltip.buckets.contains"), what + "'s tooltip");
        List<Object> args = List.of(((TranslatableContents) lines.get(0).getContents()).getArgs());
        helper.assertValueEqual(args, List.<Object>of(buckets, capacity), what + "'s count");
        assertInFullTooltip(helper, stack, "tooltip.buckets.contains");
    }

    private static void assertInFullTooltip(GameTestHelper helper, ItemStack stack, String key) {
        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, stack).contains(key), key + " is missing from " + stack.getItem() + "'s tooltip");
        }
    }
}
