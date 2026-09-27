package com.grim3212.assorted.boomerangs.gametest;

import com.grim3212.assorted.boomerangs.api.BoomerangsDamageSources;
import com.grim3212.assorted.boomerangs.common.entity.BoomerangEntity;
import com.grim3212.assorted.boomerangs.common.item.BoomerangItem;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.countInInventory;
import static com.grim3212.assorted.lib.test.TestSupport.hover;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** Boomerangs fly out and come back, and hurt as projectiles do. */
final class BoomerangTests {

    private BoomerangTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("boomerangs_fly_out_and_return", BoomerangTests::boomerangsFlyOutAndReturn);
        out.accept("boomerang_damage_is_projectile_damage", BoomerangTests::boomerangDamageIsProjectileDamage);
    }

    /**
     * Both boomerangs fly out and come back, ending up in the thrower's inventory. Thrown straight up on purpose: the
     * flight is longer than the test box, and up is the one direction with nothing in it whichever way the box turns.
     */
    private static void boomerangsFlyOutAndReturn(GameTestHelper helper) {
        assertBoomerangReturns(helper, BoomerangsItems.WOOD_BOOMERANG.get());
        assertBoomerangReturns(helper, BoomerangsItems.DIAMOND_BOOMERANG.get());
        helper.succeed();
    }

    /** In {@code #minecraft:is_projectile} as tridents are, so Projectile Protection and the like treat it as thrown. */
    private static void boomerangDamageIsProjectileDamage(GameTestHelper helper) {
        helper.assertTrue(BoomerangsDamageSources.source(helper.getLevel(), BoomerangsDamageSources.BOOMERANG, null, null).is(DamageTypeTags.IS_PROJECTILE), "boomerang damage is not projectile damage");
        helper.succeed();
    }

    private static void assertBoomerangReturns(GameTestHelper helper, BoomerangItem item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        ServerPlayer player = survivalPlayer(helper, new ItemStack(item));
        hover(helper, player, new Vec3(4.5D, 2.0D, 4.5D), -90.0F);

        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction(), id + " refused to be thrown");
        helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), id + " stayed in the hand after being thrown");

        List<BoomerangEntity> flying = helper.getLevel().getEntitiesOfClass(BoomerangEntity.class, player.getBoundingBox().inflate(4.0D));
        helper.assertValueEqual(flying.size(), 1, "boomerang entities in the world after throwing " + id);

        BoomerangEntity boomerang = flying.get(0);
        // Out for its configured range and back at half a block a tick; two hundred is generous and bounded.
        boolean returned = false;
        for (int tick = 0; tick < 200 && !returned; tick++) {
            boomerang.tick();
            returned = boomerang.isRemoved();
        }

        helper.assertTrue(returned, id + " never came back to the thrower");
        helper.assertValueEqual(countInInventory(player, item), 1, id + " in the thrower's inventory after it returned");

        helper.killAllEntitiesOfClass(BoomerangEntity.class);
        helper.getLevel().getServer().getPlayerList().remove(player);
    }
}
