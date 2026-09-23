package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The scuba suit's two halves. Each is driven by one piece's {@code inventoryTick} checking for its
 * partner, so a half worn alone, or worn dry, has to do nothing at all.
 * <p>
 * The diver is an armour stand rather than a test player. The suit reads nothing but the wearer's
 * slots and whether it is in water, and a placed test player's position is client authoritative -
 * its connection pulls it straight back out of the box before it has ticked in the pool.
 */
final class ScubaSuitTests {

    private ScubaSuitTests() {
    }

    /** In the middle of the pool, deep enough to be under the surface. */
    private static final BlockPos POOL = new BlockPos(4, 1, 4);
    /** Dry, on a pedestal in the corner, clear of the tank and of the floor it stands on. */
    private static final BlockPos SHORE = new BlockPos(0, 2, 0);
    /** Long enough for the diver to tick into the water and the suit to answer. */
    private static final int SETTLE = 10;
    /** Long enough to cross a wear interval whenever the test happens to start. */
    private static final int A_WHILE = 110;

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("scuba_mask_and_tank_let_you_breathe", ScubaSuitTests::scubaMaskAndTankLetYouBreathe);
        out.accept("scuba_legs_and_fins_make_you_swim", ScubaSuitTests::scubaLegsAndFinsMakeYouSwim);
        out.accept("scuba_halves_need_both_pieces_and_water", ScubaSuitTests::scubaHalvesNeedBothPiecesAndWater);
        out.accept("scuba_wears_down_while_it_is_working", ScubaSuitTests::scubaWearsDownWhileItIsWorking);
    }

    /**
     * A walled tank holding a diver clear of its sides. The walls are the point: an open pool of
     * source blocks spreads across the floor of the box over a hundred odd ticks and quietly wets
     * anything meant to be standing on dry land.
     */
    private static void fillPool(GameTestHelper helper) {
        for (int x = 2; x <= 6; x++) {
            for (int z = 2; z <= 6; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
                boolean wall = x == 2 || x == 6 || z == 2 || z == 6;
                for (int y = 1; y <= 3; y++) {
                    helper.setBlock(new BlockPos(x, y, z), wall ? Blocks.STONE : Blocks.WATER);
                }
            }
        }
    }

    private static LivingEntity diver(GameTestHelper helper, BlockPos at, Map<EquipmentSlot, Item> worn) {
        LivingEntity diver = helper.spawn(EntityTypes.ARMOR_STAND, at);
        worn.forEach((slot, item) -> diver.setItemSlot(slot, new ItemStack(item)));
        return diver;
    }

    private static Map<EquipmentSlot, Item> topHalf() {
        Map<EquipmentSlot, Item> worn = new LinkedHashMap<>();
        worn.put(EquipmentSlot.HEAD, ToolsItems.SCUBA_HELMET.get());
        worn.put(EquipmentSlot.CHEST, ToolsItems.SCUBA_CHESTPLATE.get());
        return worn;
    }

    private static Map<EquipmentSlot, Item> bottomHalf() {
        Map<EquipmentSlot, Item> worn = new LinkedHashMap<>();
        worn.put(EquipmentSlot.LEGS, ToolsItems.SCUBA_LEGGINGS.get());
        worn.put(EquipmentSlot.FEET, ToolsItems.SCUBA_BOOTS.get());
        return worn;
    }

    private static void assertEffect(GameTestHelper helper, LivingEntity diver, Holder<MobEffect> effect, boolean expected, String what) {
        helper.assertValueEqual(diver.hasEffect(effect), expected, what);
    }

    /** Mask and tank together: breathing and seeing, and nothing to do with swimming. */
    private static void scubaMaskAndTankLetYouBreathe(GameTestHelper helper) {
        fillPool(helper);
        LivingEntity diver = diver(helper, POOL, topHalf());

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(diver.isInWater(), "the diver never got into the water");
                    assertEffect(helper, diver, MobEffects.CONDUIT_POWER, true, "conduit power on a diver wearing the mask and tank");
                    assertEffect(helper, diver, MobEffects.DOLPHINS_GRACE, false, "dolphins grace from the top half alone");
                })
                .thenSucceed();
    }

    /** Legs and fins together: swimming, and nothing to do with breathing. */
    private static void scubaLegsAndFinsMakeYouSwim(GameTestHelper helper) {
        fillPool(helper);
        LivingEntity diver = diver(helper, POOL, bottomHalf());

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(diver.isInWater(), "the diver never got into the water");
                    assertEffect(helper, diver, MobEffects.DOLPHINS_GRACE, true, "dolphins grace on a diver wearing the legs and fins");
                    assertEffect(helper, diver, MobEffects.CONDUIT_POWER, false, "conduit power from the bottom half alone");
                })
                .thenSucceed();
    }

    /**
     * A working half runs its own pieces down, and a half that is doing nothing costs nothing. The
     * tank empties; it does not empty in a cupboard.
     */
    private static void scubaWearsDownWhileItIsWorking(GameTestHelper helper) {
        fillPool(helper);
        LivingEntity diver = diver(helper, POOL, topHalf());

        helper.setBlock(SHORE.below(), Blocks.STONE);
        LivingEntity onLand = diver(helper, SHORE, topHalf());

        helper.startSequence()
                .thenIdle(A_WHILE)
                .thenExecute(() -> {
                    for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST}) {
                        helper.assertTrue(diver.getItemBySlot(slot).getDamageValue() > 0,
                                "the " + slot.getName() + " of a working suit took no wear underwater");
                        helper.assertValueEqual(onLand.getItemBySlot(slot).getDamageValue(), 0,
                                "wear on the " + slot.getName() + " of a suit stood on dry land");
                    }
                })
                .thenSucceed();
    }

    /**
     * Half a half is nothing, and so is the whole suit on dry land - neither bonus is a property of
     * the pieces on their own.
     */
    private static void scubaHalvesNeedBothPiecesAndWater(GameTestHelper helper) {
        fillPool(helper);

        Map<EquipmentSlot, Item> maskOnly = new LinkedHashMap<>();
        maskOnly.put(EquipmentSlot.HEAD, ToolsItems.SCUBA_HELMET.get());
        LivingEntity halfDressed = diver(helper, POOL, maskOnly);

        Map<EquipmentSlot, Item> whole = new LinkedHashMap<>(topHalf());
        whole.putAll(bottomHalf());
        helper.setBlock(SHORE.below(), Blocks.STONE);
        LivingEntity onLand = diver(helper, SHORE, whole);

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(halfDressed.isInWater(), "the half dressed diver never got into the water");
                    assertEffect(helper, halfDressed, MobEffects.CONDUIT_POWER, false, "conduit power from the mask on its own");
                    assertEffect(helper, halfDressed, MobEffects.DOLPHINS_GRACE, false, "dolphins grace from the mask on its own");

                    helper.assertFalse(onLand.isInWater(), "the dry diver ended up in the water");
                    assertEffect(helper, onLand, MobEffects.CONDUIT_POWER, false, "conduit power from the whole suit on dry land");
                    assertEffect(helper, onLand, MobEffects.DOLPHINS_GRACE, false, "dolphins grace from the whole suit on dry land");
                })
                .thenSucceed();
    }
}
