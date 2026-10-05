package com.grim3212.assorted.hammers.gametest;

import com.grim3212.assorted.hammers.common.item.HammerItem;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** A hammer breaks anything in one hit for one point of durability, in every material the shared tiers have. */
final class HammerTests {

    private HammerTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("hammer_breaks_and_wears", HammerTests::hammerBreaksAndWears);
        out.accept("hammers_take_their_enchantments", HammerTests::hammersTakeTheirEnchantments);
        out.accept("every_shared_tier_has_a_hammer", HammerTests::everySharedTierHasAHammer);
    }

    private static void hammerBreaksAndWears(GameTestHelper helper) {
        final BlockPos target = new BlockPos(4, 1, 4);
        helper.setBlock(target, Blocks.OBSIDIAN);

        BlockState obsidian = Blocks.OBSIDIAN.defaultBlockState();
        ServerPlayer player = survivalPlayer(helper, new ItemStack(HammersItems.WOOD_HAMMER.get()));
        stand(helper, player, new BlockPos(4, 1, 2));

        ItemStack hammer = player.getItemInHand(InteractionHand.MAIN_HAND);
        helper.assertTrue(hammer.isCorrectToolForDrops(obsidian), "a hammer should be the correct tool for anything");
        helper.assertValueEqual(hammer.getDestroySpeed(obsidian), 80.0F, "hammer destroy speed");

        boolean destroyed = player.gameMode.destroyBlock(helper.absolutePos(target));

        helper.assertFalse(destroyed, "the hammer should break the block itself and then abort the normal destroy path");
        helper.assertBlockPresent(Blocks.AIR, target);
        helper.assertValueEqual(player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), 1, "hammer durability spent");

        helper.succeed();
    }

    private static void hammersTakeTheirEnchantments(GameTestHelper helper) {
        List<String> missing = new ArrayList<>();
        List<TagKey<Item>> tags = List.of(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.WEAPON_ENCHANTABLE, ItemTags.MELEE_WEAPON_ENCHANTABLE,
                ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE, ItemTags.DURABILITY_ENCHANTABLE, ItemTags.VANISHING_ENCHANTABLE);
        for (HammerItem hammer : HammersItems.hammers()) {
            for (TagKey<Item> tag : tags) {
                if (!new ItemStack(hammer).is(tag)) {
                    missing.add(BuiltInRegistries.ITEM.getKey(hammer) + " not in #" + tag.location());
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), missing.size() + " item/tag pairs are missing from the enchantable tags: " + String.join(", ", missing));
        helper.succeed();
    }

    /** A hammer's durability is its tier's, read from the shared tier config this mod asked Lib for. */
    private static void everySharedTierHasAHammer(GameTestHelper helper) {
        ToolTiers tiers = ToolTiers.get();
        helper.assertTrue(ToolTiers.created(), "the shared tool tiers were never made");
        tiers.extras().forEach((name, tier) -> {
            HammerItem hammer = HammersItems.EXTRA_HAMMERS.get(name).get();
            helper.assertTrue(hammer.getToolTier() == tier, name + "_hammer is not made of the shared " + name + " tier");
            helper.assertValueEqual(new ItemStack(hammer).getMaxDamage(), tier.getMaxUses(), name + "_hammer durability");
        });
        helper.assertValueEqual(new ItemStack(HammersItems.IRON_HAMMER.get()).getMaxDamage(), tiers.iron.getMaxUses(), "iron_hammer durability");
        helper.succeed();
    }
}
