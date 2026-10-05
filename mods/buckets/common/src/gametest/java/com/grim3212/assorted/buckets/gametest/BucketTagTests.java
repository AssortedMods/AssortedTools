package com.grim3212.assorted.buckets.gametest;

import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Every bucket takes the durability enchantments and is a fluid container, and every milk bucket is milk to a recipe. */
final class BucketTagTests {

    private BucketTagTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("buckets_are_in_their_tags", BucketTagTests::bucketsAreInTheirTags);
    }

    private static void bucketsAreInTheirTags(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        expect(missing, LibCommonTags.Items.BUCKETS_MILK, Items.MILK_BUCKET);
        for (BucketPair pair : BucketsItems.pairs()) {
            expect(missing, ItemTags.DURABILITY_ENCHANTABLE, pair.bucket().get());
            expect(missing, ItemTags.VANISHING_ENCHANTABLE, pair.bucket().get());
            expect(missing, LibCommonTags.Items.FLUID_CONTAINERS, pair.bucket().get());
            expect(missing, LibCommonTags.Items.BUCKETS_MILK, pair.milk().get());
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing: " + String.join(", ", missing));
        helper.succeed();
    }

    private static void expect(List<String> missing, TagKey<Item> tag, Item item) {
        if (!new ItemStack(item).is(tag)) {
            missing.add(BuiltInRegistries.ITEM.getKey(item) + " not in #" + tag.location());
        }
    }
}
