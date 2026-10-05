package com.grim3212.assorted.multitools.gametest;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.IPlatformHelper;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.multitools.common.item.MultiToolItem;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;
import static com.grim3212.assorted.lib.test.TestSupport.useOnTopOf;

/** A multitool mines what every tool mines at its material's speed, strips, paths and tills, sweeps like a sword, counts as a pickaxe of its tier, and takes both tool and weapon enchantments. */
final class MultitoolTests {

    private MultitoolTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("multitool_mines_every_tool_class", MultitoolTests::multitoolMinesEveryToolClass);
        out.accept("multitool_strips_and_paths", MultitoolTests::multitoolStripsAndPaths);
        out.accept("multitools_take_their_enchantments", MultitoolTests::multitoolsTakeTheirEnchantments);
        out.accept("multitool_tills_on_a_second_click", MultitoolTests::multitoolTillsOnASecondClick);
        out.accept("multitool_sweeps_like_a_sword", MultitoolTests::multitoolSweepsLikeASword);
        out.accept("multitool_counts_as_a_pickaxe_of_its_tier", MultitoolTests::multitoolCountsAsAPickaxeOfItsTier);
        out.accept("multitools_are_in_every_tool_tag", MultitoolTests::multitoolsAreInEveryToolTag);
    }

    /** Other mods find tools by these tags, so a multitool answers to every one of them. */
    private static void multitoolsAreInEveryToolTag(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (MultiToolItem multitool : MultitoolsItems.multitools()) {
            for (TagKey<Item> tag : List.of(ItemTags.SWORDS, ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES)) {
                if (!new ItemStack(multitool).is(tag)) {
                    missing.add(BuiltInRegistries.ITEM.getKey(multitool) + " from #" + tag.location());
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), "multitools missing from tool tags: " + String.join(", ", missing));
        helper.succeed();
    }

    /** The grinding mill's tool slot asks for an iron or better pickaxe this way, so iron and up fit and wood, stone and gold don't. */
    private static void multitoolCountsAsAPickaxeOfItsTier(GameTestHelper helper) {
        for (MultiToolItem multitool : List.of(MultitoolsItems.IRON_MULTITOOL.get(), MultitoolsItems.DIAMOND_MULTITOOL.get(), MultitoolsItems.NETHERITE_MULTITOOL.get())) {
            helper.assertTrue(Services.PLATFORM.isTieredTool(new ItemStack(multitool), IPlatformHelper.ToolTier.IRON, IPlatformHelper.ToolType.PICKAXE),
                    multitool + " should count as an iron or better pickaxe");
        }
        for (MultiToolItem multitool : List.of(MultitoolsItems.WOODEN_MULTITOOL.get(), MultitoolsItems.STONE_MULTITOOL.get(), MultitoolsItems.GOLDEN_MULTITOOL.get())) {
            helper.assertFalse(Services.PLATFORM.isTieredTool(new ItemStack(multitool), IPlatformHelper.ToolTier.IRON, IPlatformHelper.ToolType.PICKAXE),
                    multitool + " should not count as an iron pickaxe");
        }
        helper.succeed();
    }

    /** The shovel goes before the hoe, so grass and dirt turn to a path first and to farmland on the next click. */
    private static void multitoolTillsOnASecondClick(GameTestHelper helper) {
        final BlockPos grass = new BlockPos(3, 1, 4);
        final BlockPos dirt = new BlockPos(5, 1, 4);
        helper.setBlock(grass, Blocks.GRASS_BLOCK);
        helper.setBlock(dirt, Blocks.DIRT);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(MultitoolsItems.DIAMOND_MULTITOOL.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        for (BlockPos pos : List.of(grass, dirt)) {
            helper.assertTrue(useOnTopOf(helper, player, pos).consumesAction(), "the first click did nothing");
            helper.assertBlockPresent(Blocks.DIRT_PATH, pos);
            helper.assertTrue(useOnTopOf(helper, player, pos).consumesAction(), "the second click did nothing");
            helper.assertBlockPresent(Blocks.FARMLAND, pos);
        }
        helper.succeed();
    }

    /** A full strength hit sweeps the mob beside the target, on both loaders, as a sword's does. */
    private static void multitoolSweepsLikeASword(GameTestHelper helper) {
        Pig target = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(4, 1, 4));
        Pig beside = helper.spawnWithNoFreeWill(EntityTypes.PIG, new BlockPos(5, 1, 4));

        ServerPlayer player = survivalPlayer(helper, new ItemStack(MultitoolsItems.WOODEN_MULTITOOL.get()));
        stand(helper, player, new BlockPos(4, 1, 2));
        player.setOnGround(true);
        fullAttackStrength(helper, player);

        player.attack(target);
        helper.assertTrue(target.getHealth() < target.getMaxHealth(), "the multitool did not hurt the pig it hit");
        helper.assertTrue(beside.getHealth() < beside.getMaxHealth(), "the multitool did not sweep the pig beside it");
        helper.succeed();
    }

    /** Test players aren't ticked, so the attack cooldown never fills on its own. */
    private static void fullAttackStrength(GameTestHelper helper, ServerPlayer player) {
        try {
            Field ticker = LivingEntity.class.getDeclaredField("attackStrengthTicker");
            ticker.setAccessible(true);
            ticker.setInt(player, 100);
        } catch (ReflectiveOperationException e) {
            helper.fail("could not fill the attack cooldown: " + e);
        }
        helper.assertValueEqual(player.getAttackStrengthScale(0.5F), 1.0F, "attack strength");
    }

    private static void multitoolMinesEveryToolClass(GameTestHelper helper) {
        final BlockPos stone = new BlockPos(2, 1, 4);
        final BlockPos log = new BlockPos(4, 1, 4);
        final BlockPos dirt = new BlockPos(6, 1, 4);

        helper.setBlock(stone, Blocks.STONE);
        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(dirt, Blocks.DIRT);

        MultiToolItem multitool = MultitoolsItems.DIAMOND_MULTITOOL.get();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(multitool));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        // Cobweb is the one block the multitool answers for by hand rather than through the tag.
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()), 15.0F, "multitool speed on cobweb");
        float efficiency = multitool.getToolTier().getEfficiency();
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.STONE.defaultBlockState()), efficiency, "multitool speed on stone");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState()), efficiency, "multitool speed on a log");
        helper.assertValueEqual(stack.getDestroySpeed(Blocks.DIRT.defaultBlockState()), efficiency, "multitool speed on dirt");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(stone)), "the multitool could not break stone");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(log)), "the multitool could not break a log");
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(dirt)), "the multitool could not break dirt");

        helper.assertItemEntityPresent(Items.COBBLESTONE, stone, 2.0D);
        helper.assertItemEntityPresent(Items.OAK_LOG, log, 2.0D);
        helper.assertItemEntityPresent(Items.DIRT, dirt, 2.0D);

        helper.succeed();
    }

    private static void multitoolStripsAndPaths(GameTestHelper helper) {
        final BlockPos log = new BlockPos(2, 1, 4);
        final BlockPos grass = new BlockPos(4, 1, 4);

        helper.setBlock(log, Blocks.OAK_LOG);
        helper.setBlock(grass, Blocks.GRASS_BLOCK);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(MultitoolsItems.DIAMOND_MULTITOOL.get()));
        stand(helper, player, new BlockPos(3, 1, 2));

        helper.assertTrue(useOnTopOf(helper, player, log).consumesAction(), "the multitool did not act as an axe");
        helper.assertBlockPresent(Blocks.STRIPPED_OAK_LOG, log);

        helper.assertTrue(useOnTopOf(helper, player, grass).consumesAction(), "the multitool did not act as a shovel");
        helper.assertBlockPresent(Blocks.DIRT_PATH, grass);

        helper.succeed();
    }

    private static void multitoolsTakeTheirEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        List<TagKey<Item>> tags = List.of(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE,
                ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE,
                LibCommonTags.Items.TOOLS_MELEE_WEAPONS, LibCommonTags.Items.TOOLS_MINING_TOOLS);
        for (MultiToolItem multitool : MultitoolsItems.multitools()) {
            for (TagKey<Item> tag : tags) {
                if (!new ItemStack(multitool).is(tag)) {
                    missing.add(BuiltInRegistries.ITEM.getKey(multitool) + " not in #" + tag.location());
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing: " + String.join(", ", missing));
        helper.succeed();
    }
}
