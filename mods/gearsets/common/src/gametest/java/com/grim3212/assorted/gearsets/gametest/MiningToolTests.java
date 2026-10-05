package com.grim3212.assorted.gearsets.gametest;

import com.grim3212.assorted.gearsets.api.GearSetsTags;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
import com.grim3212.assorted.lib.core.tool.HarvestTiers;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.gearsets.gametest.GearSetsTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** The extra materials mine at their tier's speed and harvest level, and sit in the tags other mods look for. */
final class MiningToolTests {

    private MiningToolTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("harvest_tier_gates_drops", MiningToolTests::harvestTierGatesDrops);
        out.accept("tools_mine_at_their_tier_speed", MiningToolTests::toolsMineAtTheirTierSpeed);
        out.accept("tools_are_in_the_convention_tool_tags", MiningToolTests::toolsAreInTheConventionToolTags);
        out.accept("materials_take_their_enchantments", MiningToolTests::materialsTakeTheirEnchantments);
    }

    private static void harvestTierGatesDrops(GameTestHelper helper) {
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(0) == BlockTags.INCORRECT_FOR_WOODEN_TOOL, "harvest level 0 should map to the wooden tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(1) == BlockTags.INCORRECT_FOR_STONE_TOOL, "harvest level 1 should map to the stone tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(2) == BlockTags.INCORRECT_FOR_IRON_TOOL, "harvest level 2 should map to the iron tag");
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(3) == BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "harvest level 3 should map to the diamond tag");
        // Everything above diamond collapses onto netherite: there is nothing stronger to map to.
        helper.assertTrue(HarvestTiers.incorrectBlocksForDrops(7) == BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "harvest levels above 3 should map to the netherite tag");

        final BlockPos oreA = new BlockPos(1, 1, 6);
        final BlockPos oreB = new BlockPos(3, 1, 6);
        final BlockPos obsidianA = new BlockPos(5, 1, 6);
        final BlockPos obsidianB = new BlockPos(7, 1, 6);

        // Diamond ore, not iron: a stone level tool mines iron ore perfectly well in vanilla.
        helper.setBlock(oreA, Blocks.DIAMOND_ORE);
        helper.setBlock(oreB, Blocks.DIAMOND_ORE);
        helper.setBlock(obsidianA, Blocks.OBSIDIAN);
        helper.setBlock(obsidianB, Blocks.OBSIDIAN);

        // Tin is stone level, bronze iron level and steel diamond level by default.
        ServerPlayer player = survivalPlayer(helper, new ItemStack(pickaxe("tin")));
        stand(helper, player, new BlockPos(4, 1, 4));

        player.gameMode.destroyBlock(helper.absolutePos(oreA));
        helper.assertBlockPresent(Blocks.AIR, oreA);
        helper.assertItemEntityNotPresent(Items.DIAMOND, oreA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(pickaxe("bronze")));
        player.gameMode.destroyBlock(helper.absolutePos(oreB));
        helper.assertItemEntityPresent(Items.DIAMOND, oreB, 1.5D);

        player.gameMode.destroyBlock(helper.absolutePos(obsidianA));
        helper.assertBlockPresent(Blocks.AIR, obsidianA);
        helper.assertItemEntityNotPresent(Items.OBSIDIAN, obsidianA, 1.5D);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(pickaxe("steel")));
        player.gameMode.destroyBlock(helper.absolutePos(obsidianB));
        helper.assertItemEntityPresent(Items.OBSIDIAN, obsidianB, 1.5D);

        helper.succeed();
    }

    private static Item pickaxe(String material) {
        return GearSetsItems.MATERIALS.get(material).pickaxe().get();
    }

    private static void toolsMineAtTheirTierSpeed(GameTestHelper helper) {
        final BlockState stone = Blocks.STONE.defaultBlockState();
        final BlockState dirt = Blocks.DIRT.defaultBlockState();
        final BlockState log = Blocks.OAK_LOG.defaultBlockState();
        final BlockState hay = Blocks.HAY_BLOCK.defaultBlockState();
        // Glass is in none of the mineable tags, so every one of these falls back to bare hands.
        final BlockState glass = Blocks.GLASS.defaultBlockState();

        List<ToolTier> tiers = new ArrayList<>();
        List<Item> pickaxes = new ArrayList<>();

        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            float efficiency = set.tier().getEfficiency();
            String name = set.name();

            assertSpeed(helper, set.pickaxe().get(), stone, efficiency, name + " pickaxe on stone");
            assertSpeed(helper, set.shovel().get(), dirt, efficiency, name + " shovel on dirt");
            assertSpeed(helper, set.axe().get(), log, efficiency, name + " axe on a log");
            assertSpeed(helper, set.hoe().get(), hay, efficiency, name + " hoe on a hay block");
            assertSpeed(helper, set.pickaxe().get(), glass, 1.0F, name + " pickaxe on a block it does not mine");

            helper.assertTrue(efficiency > 1.0F, name + " is configured to mine no faster than a bare hand");

            tiers.add(set.tier());
            pickaxes.add(set.pickaxe().get());
        }

        // Sorted by configured efficiency, the measured speeds have to come out sorted too, which catches an item handed the wrong tier.
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < tiers.size(); i++) {
            order.add(i);
        }
        order.sort(Comparator.comparingDouble(i -> tiers.get(i).getEfficiency()));

        for (int i = 1; i < order.size(); i++) {
            ToolTier slower = tiers.get(order.get(i - 1));
            ToolTier faster = tiers.get(order.get(i));
            float slowSpeed = new ItemStack(pickaxes.get(order.get(i - 1))).getDestroySpeed(stone);
            float fastSpeed = new ItemStack(pickaxes.get(order.get(i))).getDestroySpeed(stone);
            helper.assertTrue(slowSpeed <= fastSpeed, slower.getName() + " mines faster than " + faster.getName() + ", the wrong way round for their configured efficiencies");
        }

        helper.succeed();
    }

    private static void toolsAreInTheConventionToolTags(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            expect(missing, LibCommonTags.Items.TOOLS_MELEE_WEAPONS, set.sword().get(), set.axe().get(), set.spear().get());
            expect(missing, LibCommonTags.Items.TOOLS_MINING_TOOLS, set.pickaxe().get());
            expect(missing, GearSetsTags.materialTools("pickaxes", set.name()), set.pickaxe().get());
            expect(missing, GearSetsTags.materialTools("spears", set.name()), set.spear().get());
        }
        // Vanilla's own materials are filled in too, for mods that ask by material.
        expect(missing, GearSetsTags.materialTools("pickaxes", "iron"), Items.IRON_PICKAXE);
        expect(missing, GearSetsTags.materialTools("swords", "wood"), Items.WOODEN_SWORD);
        helper.assertTrue(missing.isEmpty(), "missing from the convention tool tags: " + String.join(", ", missing));
        helper.succeed();
    }

    private static void materialsTakeTheirEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            for (Item tool : List.of(set.pickaxe().get(), set.shovel().get(), set.axe().get(), set.hoe().get())) {
                expect(missing, ItemTags.MINING_ENCHANTABLE, tool);
                expect(missing, ItemTags.MINING_LOOT_ENCHANTABLE, tool);
            }
            for (Item weapon : List.of(set.sword().get(), set.axe().get(), set.spear().get())) {
                expect(missing, ItemTags.WEAPON_ENCHANTABLE, weapon);
                expect(missing, ItemTags.MELEE_WEAPON_ENCHANTABLE, weapon);
                expect(missing, ItemTags.SHARP_WEAPON_ENCHANTABLE, weapon);
                expect(missing, ItemTags.FIRE_ASPECT_ENCHANTABLE, weapon);
            }
            expect(missing, ItemTags.HEAD_ARMOR_ENCHANTABLE, set.helmet().get());
            expect(missing, ItemTags.CHEST_ARMOR_ENCHANTABLE, set.chestplate().get());
            expect(missing, ItemTags.LEG_ARMOR_ENCHANTABLE, set.leggings().get());
            expect(missing, ItemTags.FOOT_ARMOR_ENCHANTABLE, set.boots().get());
            for (Item item : set.items()) {
                expect(missing, ItemTags.DURABILITY_ENCHANTABLE, item);
                expect(missing, ItemTags.VANISHING_ENCHANTABLE, item);
            }
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }
}
