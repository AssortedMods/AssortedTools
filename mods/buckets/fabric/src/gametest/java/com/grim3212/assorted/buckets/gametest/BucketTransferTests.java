package com.grim3212.assorted.buckets.gametest;

import com.grim3212.assorted.buckets.common.item.BetterBucketItem;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/**
 * A better bucket as other mods reach it, through {@code FluidStorage.ITEM}. The shared tests fill and empty it
 * through the mod's own fluid manager; these go through the Fabric wrapper, which takes amounts as longs.
 */
final class BucketTransferTests {

    private BucketTransferTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("fabric_bucket_moves_as_much_as_it_can", BucketTransferTests::bucketMovesAsMuchAsItCan);
    }

    /**
     * A caller moving as much as it can asks for {@code Long.MAX_VALUE}. The bucket takes and gives up to what it
     * holds; the amount was once cast straight to an int, which turned it into -1 and moved nothing.
     */
    private static void bucketMovesAsMuchAsItCan(GameTestHelper helper) {
        BetterBucketItem bucket = BucketsItems.GOLD.bucket().get();
        int capacity = bucket.getMaximumMillibuckets();
        ServerPlayer player = survivalPlayer(helper, bucket.getEmptyStack());
        ContainerItemContext context = ContainerItemContext.ofPlayerHand(player, InteractionHand.MAIN_HAND);
        Storage<FluidVariant> storage = context.find(FluidStorage.ITEM);
        helper.assertTrue(storage != null, "the better bucket exposed no fluid storage");

        try (Transaction transaction = Transaction.openOuter()) {
            helper.assertValueEqual(storage.insert(FluidVariant.of(Fluids.WATER), Long.MAX_VALUE, transaction), (long) capacity, "water taken when offered as much as there is");
            transaction.commit();
        }
        ItemStack filled = context.getItemVariant().toStack();
        helper.assertValueEqual(BetterBucketItem.getFluid(filled), "minecraft:water", "the fluid in the bucket after filling");
        helper.assertValueEqual(BetterBucketItem.getAmount(filled), capacity, "water held after filling");

        try (Transaction transaction = Transaction.openOuter()) {
            helper.assertValueEqual(storage.extract(FluidVariant.of(Fluids.WATER), Long.MAX_VALUE, transaction), (long) capacity, "water given up when asked for as much as there is");
            transaction.commit();
        }
        helper.assertValueEqual(BetterBucketItem.getAmount(context.getItemVariant().toStack()), 0, "water held after emptying");
        helper.succeed();
    }
}
