package com.grim3212.assorted.wands.gametest;

import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.wands.common.item.WandModeInfo;
import com.grim3212.assorted.wands.common.item.WandsDataComponents;
import com.grim3212.assorted.wands.common.item.WandsItems;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/** A wand says which mode it is in, under the keys it had in Assorted Tools, and takes the durability enchantments. */
final class WandTooltipTests {

    private WandTooltipTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("wand_tooltip_shows_its_mode", WandTooltipTests::wandTooltipShowsItsMode);
        out.accept("wands_take_the_durability_enchantments", WandTooltipTests::wandsTakeTheDurabilityEnchantments);
    }

    private static void wandTooltipShowsItsMode(GameTestHelper helper) {
        DataComponentType<WandModeInfo> type = WandsDataComponents.WAND_MODE_INFO.get();
        Map<Item, String> modes = Map.of(
                WandsItems.BUILDING_WAND.get(), "buildbox", WandsItems.REINFORCED_BUILDING_WAND.get(), "buildcaves",
                WandsItems.BREAKING_WAND.get(), "breakweak", WandsItems.REINFORCED_BREAKING_WAND.get(), "breakxores",
                WandsItems.MINING_WAND.get(), "mineall", WandsItems.REINFORCED_MINING_WAND.get(), "mineores");

        modes.forEach((wand, mode) -> {
            ItemStack stack = new ItemStack(wand);
            helper.assertValueEqual(tooltipKeys(helper, stack, type), List.of("assortedtools.wand.broken"), wand + "'s tooltip with no mode");

            NBTHelper.putString(stack, "Mode", mode);
            List<Component> lines = tooltipLines(helper, stack, type);
            helper.assertValueEqual(lines.stream().map(TestSupport::tooltipKey).toList(), List.of("assortedtools.wand.current"), wand + "'s tooltip");
            Object shown = ((TranslatableContents) lines.get(0).getContents()).getArgs()[0];
            helper.assertValueEqual(tooltipKey((Component) shown), "assortedtools.wand.mode." + mode, wand + "'s mode in its tooltip");
            assertInFullTooltip(helper, stack, "assortedtools.wand.current");
        });
        helper.succeed();
    }

    private static void wandsTakeTheDurabilityEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        for (Item wand : WandsItems.wands()) {
            if (!new ItemStack(wand).is(ItemTags.DURABILITY_ENCHANTABLE) || !new ItemStack(wand).is(ItemTags.VANISHING_ENCHANTABLE)) {
                missing.add(BuiltInRegistries.ITEM.getKey(wand).toString());
            }
        }
        helper.assertTrue(missing.isEmpty(), "wands missing from the durability enchantable tags: " + missing);
        helper.succeed();
    }

    private static void assertInFullTooltip(GameTestHelper helper, ItemStack stack, String key) {
        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, stack).contains(key), key + " is missing from " + stack.getItem() + "'s tooltip");
        }
    }
}
