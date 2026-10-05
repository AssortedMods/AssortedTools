package com.grim3212.assorted.shears.gametest;

import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/**
 * Material shears: sheep, leaves and the Coral Cutter enchantment.
 */
final class ShearsTests {

    private ShearsTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("shears_shear_a_sheep", ShearsTests::shearsShearASheep);
        out.accept("shears_cut_leaves", ShearsTests::shearsCutLeaves);
        out.accept("shears_cut_coral_with_coral_cutter", ShearsTests::shearsCutCoralWithCoralCutter);
        out.accept("coral_cutter_covers_every_coral", ShearsTests::coralCutterCoversEveryCoral);
    }

    /**
     * Vanilla checks {@code is(Items.SHEARS)} directly, so this exercises
     * {@code MaterialShears#interactLivingEntity} on both loaders.
     */
    private static void shearsShearASheep(GameTestHelper helper) {
        final BlockPos where = new BlockPos(4, 1, 4);
        Sheep sheep = helper.spawn(EntityTypes.SHEEP, where);
        helper.assertTrue(sheep.readyForShearing(), "a freshly spawned sheep should be shearable");

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ShearsItems.DIAMOND_SHEARS.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        InteractionResult result = player.getItemInHand(InteractionHand.MAIN_HAND).interactLivingEntity(player, sheep, InteractionHand.MAIN_HAND);

        helper.assertTrue(result != InteractionResult.PASS, "modded shears were ignored by the sheep");
        helper.assertFalse(sheep.readyForShearing(), "the sheep was not sheared");
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "shears durability spent");

        boolean wool = helper.getEntities(EntityTypes.ITEM, where, 4.0D).stream().anyMatch(item -> item.getItem().is(ItemTags.WOOL));
        helper.assertTrue(wool, "shearing dropped no wool");

        helper.succeed();
    }

    /**
     * Vanilla's leaves loot table checks item identity for {@code minecraft:shears}; NeoForge checks
     * the {@code shears_dig} ability instead, and Fabric relies on this mod's {@code ItemPredicate} mixin.
     */
    private static void shearsCutLeaves(GameTestHelper helper) {
        final BlockPos leaves = new BlockPos(4, 1, 4);
        // Persistent, so the block update from setBlock cannot schedule it to decay instead.
        helper.setBlock(leaves, Blocks.OAK_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true));

        ServerPlayer player = survivalPlayer(helper, new ItemStack(ShearsItems.DIAMOND_SHEARS.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDestroySpeed(helper.getBlockState(leaves)), 15.0F, "shears speed on leaves");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(leaves)), "the shears could not break leaves");
        helper.assertItemEntityPresent(Items.OAK_LEAVES, leaves, 2.0D);
        helper.assertItemEntityNotPresent(Items.OAK_SAPLING, leaves, 2.0D);

        helper.succeed();
    }

    /**
     * Coral Cutter spans three pieces: a mixin for speed, {@code CorrectToolForDropEvent} for the
     * harvest check, and {@code OnDropStacksEvent} to re-roll live coral drops with silk touch.
     */
    private static void shearsCutCoralWithCoralCutter(GameTestHelper helper) {
        final BlockPos plainTarget = new BlockPos(2, 1, 4);
        final BlockPos cutterTarget = new BlockPos(6, 1, 4);
        helper.setBlock(plainTarget, Blocks.TUBE_CORAL_BLOCK);
        helper.setBlock(cutterTarget, Blocks.TUBE_CORAL_BLOCK);

        BlockState coral = Blocks.TUBE_CORAL_BLOCK.defaultBlockState();
        Holder<Enchantment> coralCutter = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ShearsEnchantments.CORAL_CUTTER);

        ItemStack plain = new ItemStack(ShearsItems.DIAMOND_SHEARS.get());
        ItemStack cutter = new ItemStack(ShearsItems.DIAMOND_SHEARS.get());
        cutter.enchant(coralCutter, 1);
        helper.assertTrue(ShearsEnchantments.hasCoralCutter(cutter), "the enchantment did not land on the shears");

        // Also proves both loaders load assortedshears.mixins.json; unenchanted shears are measured first so a mixin that boosted everything, or never ran, can't pass.
        helper.assertValueEqual(plain.getDestroySpeed(coral), 1.0F, "plain shears speed on coral");
        helper.assertValueEqual(cutter.getDestroySpeed(coral), 10.0F, "coral cutter shears speed on coral");
        helper.assertFalse(plain.isCorrectToolForDrops(coral), "plain shears should not harvest coral");
        helper.assertTrue(cutter.isCorrectToolForDrops(coral), "coral cutter shears should harvest coral");

        ServerPlayer player = survivalPlayer(helper, plain);
        stand(helper, player, new BlockPos(4, 1, 2));

        player.gameMode.destroyBlock(helper.absolutePos(plainTarget));
        helper.assertBlockPresent(Blocks.AIR, plainTarget);
        helper.assertItemEntityNotPresent(Items.TUBE_CORAL_BLOCK, plainTarget, 2.0D);

        player.setItemInHand(InteractionHand.MAIN_HAND, cutter);
        player.gameMode.destroyBlock(helper.absolutePos(cutterTarget));
        helper.assertBlockPresent(Blocks.AIR, cutterTarget);
        // Live coral, not the dead block a pickaxe without silk touch would leave behind.
        helper.assertItemEntityPresent(Items.TUBE_CORAL_BLOCK, cutterTarget, 2.0D);
        helper.assertItemEntityNotPresent(Items.DEAD_TUBE_CORAL_BLOCK, cutterTarget, 2.0D);

        helper.succeed();
    }

    /**
     * Walks the block registry so a newly added coral can't slip past {@code c:corals/all}, then
     * breaks a dead coral block and fan, neither of which plain shears could harvest.
     */
    private static void coralCutterCoversEveryCoral(GameTestHelper helper) {
        Holder<Enchantment> coralCutter = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ShearsEnchantments.CORAL_CUTTER);
        ItemStack cutter = new ItemStack(ShearsItems.DIAMOND_SHEARS.get());
        cutter.enchant(coralCutter, 1);

        List<String> missed = new ArrayList<>();
        BuiltInRegistries.BLOCK.listElements()
                .filter(block -> block.key().identifier().getNamespace().equals("minecraft") && block.key().identifier().getPath().contains("coral"))
                .forEach(block -> {
                    BlockState state = block.value().defaultBlockState();
                    if (!state.is(ShearsTags.ALL_CORALS) || cutter.getDestroySpeed(state) != 10.0F || !cutter.isCorrectToolForDrops(state)) {
                        missed.add(block.key().identifier().getPath());
                    }
                });
        helper.assertTrue(missed.isEmpty(), "coral cutter does not cover: " + String.join(", ", missed));

        final BlockPos deadBlock = new BlockPos(2, 1, 4);
        final BlockPos deadFan = new BlockPos(6, 1, 4);
        helper.setBlock(deadBlock, Blocks.DEAD_TUBE_CORAL_BLOCK);
        helper.setBlock(deadFan, Blocks.DEAD_TUBE_CORAL_FAN);

        ServerPlayer player = survivalPlayer(helper, cutter);
        stand(helper, player, new BlockPos(4, 1, 2));
        player.gameMode.destroyBlock(helper.absolutePos(deadBlock));
        player.gameMode.destroyBlock(helper.absolutePos(deadFan));

        helper.assertItemEntityPresent(Items.DEAD_TUBE_CORAL_BLOCK, deadBlock, 2.0D);
        helper.assertItemEntityPresent(Items.DEAD_TUBE_CORAL_FAN, deadFan, 2.0D);
        helper.succeed();
    }
}
