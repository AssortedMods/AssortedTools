package com.grim3212.assorted.throwingspears.gametest;

import com.grim3212.assorted.throwingspears.api.ThrowingSpearsDamageSources;
import com.grim3212.assorted.throwingspears.common.entity.BetterSpearEntity;
import com.grim3212.assorted.throwingspears.common.entity.ThrowingSpearsEntities;
import com.grim3212.assorted.throwingspears.common.item.BetterSpearItem;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.countInInventory;
import static com.grim3212.assorted.lib.test.TestSupport.hover;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** A thrown spear sticks where it lands, can be picked back up, hurts what it hits, and hurts as a projectile. */
final class ThrowingSpearTests {

    private ThrowingSpearTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("spear_sticks_in_a_block_and_is_picked_up", ThrowingSpearTests::spearSticksInABlockAndIsPickedUp);
        out.accept("spear_damages_a_mob", ThrowingSpearTests::spearDamagesAMob);
        out.accept("spear_damage_is_projectile_damage", ThrowingSpearTests::spearDamageIsProjectileDamage);
    }

    private static void spearSticksInABlockAndIsPickedUp(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()));
        hover(helper, player, new Vec3(4.5D, 4.0D, 4.5D), 90.0F);

        BetterSpearEntity spear = throwHeldSpear(helper, player);

        // Straight down from four blocks up at 2.5 a tick: a handful of ticks, and a bounded loop rather than physics the test waits on.
        boolean landed = tickUntil(spear, 40, () -> spear.shakeTime > 0);
        helper.assertTrue(landed, "the spear never stuck in the floor");
        helper.assertFalse(spear.isRemoved(), "the spear vanished instead of sticking");

        // shakeTime counts back down to zero before an arrow can be collected.
        tickUntil(spear, 20, () -> spear.shakeTime <= 0);
        spear.playerTouch(player);

        helper.assertTrue(spear.isRemoved(), "the spear was not collected");
        helper.assertValueEqual(countInInventory(player, ThrowingSpearsItems.IRON_THROWING_SPEAR.get()), 1, "spears in the inventory after picking it back up");

        helper.succeed();
    }

    /** The same throw, into a cow. */
    private static void spearDamagesAMob(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 4));
        float before = cow.getHealth();

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ThrowingSpearsItems.IRON_THROWING_SPEAR.get()));
        hover(helper, player, new Vec3(4.5D, 5.0D, 4.5D), 90.0F);

        BetterSpearEntity spear = throwHeldSpear(helper, player);
        boolean hit = tickUntil(spear, 40, () -> cow.getHealth() < before);

        helper.assertTrue(hit, "the spear did not damage the cow");
        helper.assertTrue(cow.isAlive(), "an iron spear should not kill a cow outright");

        helper.succeed();
    }

    /** In {@code #minecraft:is_projectile} as tridents are, so Projectile Protection and the like treat it as thrown. */
    private static void spearDamageIsProjectileDamage(GameTestHelper helper) {
        helper.assertTrue(ThrowingSpearsDamageSources.source(helper.getLevel(), ThrowingSpearsDamageSources.SPEAR, null, null).is(DamageTypeTags.IS_PROJECTILE), "spear damage is not projectile damage");
        helper.succeed();
    }

    /** Throws whatever spear is in the main hand through the item's own charge and release. */
    private static BetterSpearEntity throwHeldSpear(GameTestHelper helper, ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        BetterSpearItem spear = (BetterSpearItem) stack.getItem();

        player.startUsingItem(InteractionHand.MAIN_HAND);
        // releaseUsing counts backwards: twenty short of the full duration is a twenty tick charge, past the ten it insists on.
        boolean thrown = spear.releaseUsing(stack, helper.getLevel(), player, spear.getUseDuration(stack, player) - 20);
        helper.assertTrue(thrown, "the spear refused to be thrown");

        List<BetterSpearEntity> spears = helper.getEntities(ThrowingSpearsEntities.BETTER_SPEAR.get());
        helper.assertValueEqual(spears.size(), 1, "spear entities in the world after one throw");
        return spears.get(0);
    }

    /** Bounded on purpose: a test that waits on physics it does not drive is a test that hangs. */
    private static boolean tickUntil(Entity entity, int limit, BooleanSupplier done) {
        for (int tick = 0; tick < limit; tick++) {
            if (done.getAsBoolean()) {
                return true;
            }
            entity.tick();
        }
        return done.getAsBoolean();
    }
}
