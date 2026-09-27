package com.grim3212.assorted.multitools.gametest;

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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;
import static com.grim3212.assorted.lib.test.TestSupport.useOnTopOf;

/** A multitool mines what every tool mines at its material's speed, strips and paths, and takes both tool and weapon enchantments. */
final class MultitoolTests {

    private MultitoolTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("multitool_mines_every_tool_class", MultitoolTests::multitoolMinesEveryToolClass);
        out.accept("multitool_strips_and_paths", MultitoolTests::multitoolStripsAndPaths);
        out.accept("multitools_take_their_enchantments", MultitoolTests::multitoolsTakeTheirEnchantments);
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
