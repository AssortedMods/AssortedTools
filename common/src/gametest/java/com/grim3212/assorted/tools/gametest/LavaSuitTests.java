package com.grim3212.assorted.tools.gametest;

import com.grim3212.assorted.tools.common.effect.ToolsMobEffects;
import com.grim3212.assorted.tools.common.item.ToolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The lava suit's halves. The diver is an armour stand for the reason {@link ScubaSuitTests} gives,
 * and being fire immune it is asked what the suit granted, not whether it survived.
 */
final class LavaSuitTests {

    private LavaSuitTests() {
    }

    private static final BlockPos POOL = new BlockPos(4, 1, 4);
    private static final BlockPos SHORE = new BlockPos(0, 2, 0);
    /** Soul fire, which stays lit on soul soil rather than burning itself out mid test. */
    private static final BlockPos FIRE = new BlockPos(0, 2, 7);
    private static final BlockPos MAGMA = new BlockPos(3, 2, 7);
    private static final BlockPos CAMPFIRE = new BlockPos(6, 2, 7);
    /** Two small tanks side by side, for comparing what lava and water each cost a suit. */
    private static final BlockPos LAVA_TANK = new BlockPos(1, 1, 4);
    private static final BlockPos WATER_TANK = new BlockPos(7, 1, 4);
    /** A second pocket of lava, for asking two differently dressed divers the same question. */
    private static final BlockPos SECOND_TANK = new BlockPos(7, 1, 7);
    private static final int SETTLE = 10;
    /** Long enough to cross a wear interval whenever the test happens to start. */
    private static final int A_WHILE = 110;
    /** Long enough for a shove to run itself out under either drag. */
    private static final int COAST = 30;

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("lava_suit_keeps_the_heat_off_only_when_whole", LavaSuitTests::lavaSuitKeepsTheHeatOffOnlyWhenWhole);
        out.accept("lava_legs_and_boots_let_you_swim", LavaSuitTests::lavaLegsAndBootsLetYouSwim);
        out.accept("lava_striding_swims_like_water", LavaSuitTests::lavaStridingSwimsLikeWater);
        out.accept("lava_halves_need_both_pieces_and_lava", LavaSuitTests::lavaHalvesNeedBothPiecesAndLava);
        out.accept("lava_suit_wears_at_the_scuba_suits_rate", LavaSuitTests::lavaSuitWearsAtTheScubaSuitsRate);
        out.accept("lava_suit_never_catches_fire", LavaSuitTests::lavaSuitNeverCatchesFire);
        out.accept("lava_suit_wears_wherever_it_burns", LavaSuitTests::lavaSuitWearsWhereverItBurns);
        out.accept("lava_boots_alone_see_off_a_hot_floor", LavaSuitTests::lavaBootsAloneSeeOffAHotFloor);
    }

    /** A walled tank of lava, for the same reason the scuba tests wall their pool. */
    private static void fillPool(GameTestHelper helper) {
        for (int x = 2; x <= 6; x++) {
            for (int z = 2; z <= 6; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
                boolean wall = x == 2 || x == 6 || z == 2 || z == 6;
                for (int y = 1; y <= 3; y++) {
                    helper.setBlock(new BlockPos(x, y, z), wall ? Blocks.STONE : Blocks.LAVA);
                }
            }
        }
    }

    /** A one block tank of {@code fluid}, walled so it cannot run out across the floor. */
    private static void tank(GameTestHelper helper, BlockPos middle, net.minecraft.world.level.block.Block fluid) {
        for (int x = middle.getX() - 1; x <= middle.getX() + 1; x++) {
            for (int z = middle.getZ() - 1; z <= middle.getZ() + 1; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
                boolean wall = x != middle.getX() || z != middle.getZ();
                for (int y = 1; y <= 3; y++) {
                    helper.setBlock(new BlockPos(x, y, z), wall ? Blocks.STONE : fluid);
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
        worn.put(EquipmentSlot.HEAD, ToolsItems.LAVA_HELMET.get());
        worn.put(EquipmentSlot.CHEST, ToolsItems.LAVA_CHESTPLATE.get());
        return worn;
    }

    private static Map<EquipmentSlot, Item> wholeSuit() {
        Map<EquipmentSlot, Item> worn = new LinkedHashMap<>(topHalf());
        worn.putAll(bottomHalf());
        return worn;
    }

    private static Map<EquipmentSlot, Item> bottomHalf() {
        Map<EquipmentSlot, Item> worn = new LinkedHashMap<>();
        worn.put(EquipmentSlot.LEGS, ToolsItems.LAVA_LEGGINGS.get());
        worn.put(EquipmentSlot.FEET, ToolsItems.LAVA_BOOTS.get());
        return worn;
    }

    private static void assertEffect(GameTestHelper helper, LivingEntity diver, Holder<MobEffect> effect, boolean expected, String what) {
        helper.assertValueEqual(diver.hasEffect(effect), expected, what);
    }

    /**
     * Lava touches all of a wearer at once, so half a suit keeps none of it off. Only the whole four
     * pieces grant fire resistance; the top two on their own leave the wearer burning from the waist
     * down and must not pretend otherwise.
     */
    private static void lavaSuitKeepsTheHeatOffOnlyWhenWhole(GameTestHelper helper) {
        fillPool(helper);
        tank(helper, SECOND_TANK, Blocks.LAVA);

        Map<EquipmentSlot, Item> whole = wholeSuit();
        LivingEntity dressed = diver(helper, POOL, whole);
        LivingEntity halfDressed = diver(helper, SECOND_TANK, topHalf());

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(dressed.isInLava(), "the diver in the whole suit never got into the lava");
                    helper.assertTrue(halfDressed.isInLava(), "the half dressed diver never got into the lava");

                    assertEffect(helper, dressed, MobEffects.FIRE_RESISTANCE, true, "fire resistance on a diver in the whole suit");
                    assertEffect(helper, halfDressed, MobEffects.FIRE_RESISTANCE, false, "fire resistance from the hood and suit alone");
                })
                .thenSucceed();
    }

    /** The legs and boots grant the striding, and grant nothing else. */
    private static void lavaLegsAndBootsLetYouSwim(GameTestHelper helper) {
        fillPool(helper);
        LivingEntity diver = diver(helper, POOL, bottomHalf());

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(diver.isInLava(), "the diver never got into the lava");
                    assertEffect(helper, diver, ToolsMobEffects.LAVA_STRIDING.asHolder(), true, "lava striding on a diver wearing the legs and boots");
                    assertEffect(helper, diver, MobEffects.FIRE_RESISTANCE, false, "fire resistance from the bottom half alone");
                })
                .thenSucceed();
    }

    /**
     * Two pigs shoved down a lava trough with the same push, one striding. Fails if
     * {@code LivingEntityLavaSwimMixin} stops applying, which nothing else would catch.
     */
    private static void lavaStridingSwimsLikeWater(GameTestHelper helper) {
        trough(helper);

        Pig striding = shove(helper, new BlockPos(2, 1, 1), true);
        Pig wading = shove(helper, new BlockPos(6, 1, 1), false);
        double stridingFrom = striding.getZ();
        double wadingFrom = wading.getZ();

        helper.startSequence()
                .thenIdle(COAST)
                .thenExecute(() -> {
                    helper.assertTrue(striding.isAlive() && wading.isAlive(), "a pig burned up before it could finish coasting");

                    double strode = striding.getZ() - stridingFrom;
                    double waded = wading.getZ() - wadingFrom;
                    helper.assertTrue(waded > 0.0D, "the wading pig never moved, so there is nothing to compare against");
                    helper.assertTrue(strode > waded * 1.5D,
                            "a striding pig coasted " + String.format("%.2f", strode) + " blocks against a wading pig's "
                                    + String.format("%.2f", waded) + "; lava is still treacle, so the mixin is not applying");
                })
                .thenSucceed();
    }

    /** A walled trough of lava for a pig to be pushed down. */
    private static void trough(GameTestHelper helper) {
        for (int x = 0; x <= 8; x++) {
            for (int z = 0; z <= 8; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
                boolean wall = x == 0 || x == 8 || z == 0 || z == 8 || x == 4;
                for (int y = 1; y <= 3; y++) {
                    helper.setBlock(new BlockPos(x, y, z), wall ? Blocks.STONE : Blocks.LAVA);
                }
            }
        }
    }

    /**
     * A pig shoved down the trough. Fire resistant so the lava cannot end the experiment early, and
     * with no free will so nothing but the push and the drag decides where it ends up.
     */
    private static Pig shove(GameTestHelper helper, BlockPos at, boolean striding) {
        Pig pig = helper.spawnWithNoFreeWill(EntityTypes.PIG, at);
        pig.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0, true, false, false));
        if (striding) {
            pig.addEffect(new MobEffectInstance(ToolsMobEffects.LAVA_STRIDING.asHolder(), 400, 0, true, false, false));
        }

        pig.setDeltaMovement(0.0D, 0.0D, 0.45D);
        pig.hurtMarked = true;
        return pig;
    }

    private static void lavaHalvesNeedBothPiecesAndLava(GameTestHelper helper) {
        fillPool(helper);

        Map<EquipmentSlot, Item> hoodOnly = new LinkedHashMap<>();
        hoodOnly.put(EquipmentSlot.HEAD, ToolsItems.LAVA_HELMET.get());
        LivingEntity halfDressed = diver(helper, POOL, hoodOnly);

        Map<EquipmentSlot, Item> whole = wholeSuit();
        helper.setBlock(SHORE.below(), Blocks.STONE);
        LivingEntity onLand = diver(helper, SHORE, whole);

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(halfDressed.isInLava(), "the half dressed diver never got into the lava");
                    assertEffect(helper, halfDressed, MobEffects.FIRE_RESISTANCE, false, "fire resistance from the hood on its own");
                    assertEffect(helper, halfDressed, ToolsMobEffects.LAVA_STRIDING.asHolder(), false, "lava striding from the hood on its own");

                    // The whole suit keeps the heat off wherever it is worn, so dry land is no
                    // exception; only the swimming is put away when there is nothing to swim through.
                    helper.assertFalse(onLand.isInLava(), "the dry diver ended up in the lava");
                    assertEffect(helper, onLand, MobEffects.FIRE_RESISTANCE, true, "fire resistance from the whole suit on dry land");
                    assertEffect(helper, onLand, ToolsMobEffects.LAVA_STRIDING.asHolder(), false, "lava striding from the whole suit on dry land");
                })
                .thenSucceed();
    }

    /**
     * The whole suit does not burn, lit by hand or stood in fire. The half dressed diver is the
     * control, or a box that ignited nobody would pass.
     */
    private static void lavaSuitNeverCatchesFire(GameTestHelper helper) {
        helper.setBlock(SHORE.below(), Blocks.STONE);
        helper.setBlock(FIRE.below(), Blocks.SOUL_SOIL);
        helper.setBlock(FIRE, Blocks.SOUL_FIRE);

        Map<EquipmentSlot, Item> whole = wholeSuit();
        LivingEntity dressed = diver(helper, SHORE, whole);
        LivingEntity halfDressed = diver(helper, FIRE, topHalf());
        dressed.igniteForSeconds(8.0F);

        helper.startSequence()
                .thenIdle(SETTLE)
                .thenExecute(() -> {
                    helper.assertTrue(halfDressed.isOnFire(), "the half dressed diver never caught fire, so this proves nothing");
                    helper.assertFalse(dressed.isOnFire(), "a diver in the whole suit is on fire");
                    helper.assertTrue(dressed.fireImmune(), "a diver in the whole suit is not fire immune");
                    assertEffect(helper, dressed, MobEffects.FIRE_RESISTANCE, true, "fire resistance on a diver in the whole suit");
                })
                .thenSucceed();
    }

    /**
     * Fire charges the whole suit; a hot floor charges the boots and nothing else, even when the
     * rest of the suit is on. Magma is the one stood on rather than in, which is why it went
     * uncharged at first.
     */
    private static void lavaSuitWearsWhereverItBurns(GameTestHelper helper) {
        helper.setBlock(FIRE.below(), Blocks.SOUL_SOIL);
        helper.setBlock(FIRE, Blocks.SOUL_FIRE);
        helper.setBlock(MAGMA.below(), Blocks.MAGMA_BLOCK);
        helper.setBlock(CAMPFIRE.below(), Blocks.STONE);
        helper.setBlock(CAMPFIRE, Blocks.CAMPFIRE);

        LivingEntity inFire = diver(helper, FIRE, wholeSuit());
        LivingEntity onMagma = diver(helper, MAGMA, bottomHalf());
        LivingEntity inCampfire = diver(helper, CAMPFIRE, wholeSuit());

        helper.startSequence()
                .thenIdle(A_WHILE)
                .thenExecute(() -> {
                    helper.assertTrue(helper.getBlockState(FIRE).is(Blocks.SOUL_FIRE), "the fire went out, so the suit had nothing to stand in");
                    assertWorn(helper, inFire, EquipmentSlot.HEAD, "stood in fire");
                    assertWorn(helper, inFire, EquipmentSlot.FEET, "stood in fire");
                    assertWorn(helper, onMagma, EquipmentSlot.FEET, "stood on magma in the boots alone");
                    assertWorn(helper, inCampfire, EquipmentSlot.FEET, "stood in a lit campfire");
                    helper.assertValueEqual(inCampfire.getItemBySlot(EquipmentSlot.HEAD).getDamageValue(), 0,
                            "wear on the hood of a suit stood in a campfire, which only ever burns the feet");
                })
                .thenSucceed();
    }

    private static void assertWorn(GameTestHelper helper, LivingEntity diver, EquipmentSlot slot, String where) {
        helper.assertTrue(diver.getItemBySlot(slot).getDamageValue() > 0,
                "the " + slot.getName() + " of a suit " + where + " for " + A_WHILE + " ticks is undamaged");
    }

    /**
     * The boots alone see off magma and campfires, and see off nothing else. Asked of
     * {@code isInvulnerableTo} rather than by burning a pig, which has a damage cooldown.
     */
    private static void lavaBootsAloneSeeOffAHotFloor(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        DamageSource hotFloor = level.damageSources().hotFloor();
        DamageSource campfire = level.damageSources().campfire();
        DamageSource lava = level.damageSources().lava();

        Pig booted = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHORE);
        booted.setItemSlot(EquipmentSlot.FEET, new ItemStack(ToolsItems.LAVA_BOOTS.get()));
        Pig bare = helper.spawnWithNoFreeWill(EntityTypes.PIG, SHORE);

        helper.assertTrue(booted.isInvulnerableTo(level, hotFloor), "lava boots did not see off a hot floor");
        helper.assertTrue(booted.isInvulnerableTo(level, campfire), "lava boots did not see off a campfire");
        helper.assertFalse(booted.isInvulnerableTo(level, lava), "lava boots saw off lava, which takes the whole suit");
        helper.assertFalse(bare.isInvulnerableTo(level, hotFloor), "a bare pig is already immune to a hot floor, so this proves nothing");
        helper.succeed();
    }

    /**
     * The two suits wear at the same rate. The lava suit is expensive enough to build without the
     * lava charging for it a second time, so the same stretch in the fluid has to cost each of them
     * the same, and this asks both at once rather than trusting two constants to stay in step.
     */
    private static void lavaSuitWearsAtTheScubaSuitsRate(GameTestHelper helper) {
        tank(helper, LAVA_TANK, Blocks.LAVA);
        tank(helper, WATER_TANK, Blocks.WATER);

        Map<EquipmentSlot, Item> whole = wholeSuit();
        LivingEntity inLava = diver(helper, LAVA_TANK, whole);
        Map<EquipmentSlot, Item> scuba = new LinkedHashMap<>();
        scuba.put(EquipmentSlot.HEAD, ToolsItems.SCUBA_HELMET.get());
        scuba.put(EquipmentSlot.CHEST, ToolsItems.SCUBA_CHESTPLATE.get());
        LivingEntity inWater = diver(helper, WATER_TANK, scuba);

        helper.startSequence()
                .thenIdle(A_WHILE)
                .thenExecute(() -> {
                    helper.assertTrue(inLava.isInLava(), "the lava diver never got into the lava");
                    helper.assertTrue(inWater.isInWater(), "the scuba diver never got into the water");

                    int lavaWorn = inLava.getItemBySlot(EquipmentSlot.HEAD).getDamageValue();
                    int scubaWorn = inWater.getItemBySlot(EquipmentSlot.HEAD).getDamageValue();
                    helper.assertTrue(scubaWorn > 0, "neither suit wore at all in " + A_WHILE + " ticks, so this proves nothing");
                    helper.assertValueEqual(lavaWorn, scubaWorn, "wear on the lava hood against the scuba mask over the same stretch");
                })
                .thenSucceed();
    }
}
