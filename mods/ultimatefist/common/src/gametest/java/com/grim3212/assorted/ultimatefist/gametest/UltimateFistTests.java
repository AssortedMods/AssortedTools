package com.grim3212.assorted.ultimatefist.gametest;

import com.grim3212.assorted.lib.core.item.LibDataComponents;
import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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

import static com.grim3212.assorted.lib.test.TestSupport.*;

/** The fist mines anything and can't be enchanted, and every fragment describes itself. */
final class UltimateFistTests {

    private UltimateFistTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("ultimate_fist_breaks_obsidian", UltimateFistTests::ultimateFistBreaksObsidian);
        out.accept("ultimate_fist_cannot_be_enchanted", UltimateFistTests::ultimateFistCannotBeEnchanted);
        out.accept("fragment_tooltip_describes_it", UltimateFistTests::fragmentTooltipDescribesIt);
    }

    private static void ultimateFistBreaksObsidian(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        ServerPlayer player = survivalPlayer(helper, new ItemStack(UltimateFistItems.ULTIMATE_FIST.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack fist = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(fist.isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()), "the ultimate fist should mine anything");
        helper.assertTrue(fist.getDestroySpeed(Blocks.OBSIDIAN.defaultBlockState()) > 1.0F, "the ultimate fist has no mining speed");

        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(target)), "the ultimate fist could not break obsidian");
        helper.assertItemEntityPresent(Items.OBSIDIAN, target, 2.0D);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "ultimate fist durability spent");

        helper.succeed();
    }

    private static void ultimateFistCannotBeEnchanted(GameTestHelper helper) {
        List<String> found = new ArrayList<>();
        ItemStack fist = new ItemStack(UltimateFistItems.ULTIMATE_FIST.get());
        for (TagKey<Item> tag : List.of(ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE,
                ItemTags.FIRE_ASPECT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE)) {
            if (fist.is(tag)) {
                found.add("#" + tag.location());
            }
        }
        helper.assertTrue(found.isEmpty(), "the ultimate fist can take enchantments from " + String.join(", ", found));
        helper.assertFalse(fist.has(DataComponents.ENCHANTABLE), "the ultimate fist can go in an enchanting table");
        helper.succeed();
    }

    private static void fragmentTooltipDescribesIt(GameTestHelper helper) {
        for (Item fragment : UltimateFistItems.fragments()) {
            ItemStack stack = new ItemStack(fragment);
            helper.assertValueEqual(tooltipKeys(helper, stack, LibDataComponents.DESCRIPTION.get()), List.of(FragmentItem.DESCRIPTION_KEY), fragment + "'s description");
            if (onNeoForge()) {
                helper.assertTrue(fullTooltipKeys(helper, stack).contains(FragmentItem.DESCRIPTION_KEY), FragmentItem.DESCRIPTION_KEY + " is missing from " + fragment + "'s tooltip");
            }
        }
        helper.succeed();
    }
}
