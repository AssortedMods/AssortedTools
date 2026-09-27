package com.grim3212.assorted.machetes.gametest;

import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.machetes.common.item.MacheteItem;
import com.grim3212.assorted.machetes.common.item.MachetesItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Machetes: one per material, cutting plants at the material's speed and swinging like a lighter sword. */
final class MacheteTests {

    private MacheteTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("machete_cuts_plants_at_its_material_speed", MacheteTests::macheteCutsPlantsAtItsMaterialSpeed);
        out.accept("machete_swings_faster_and_lighter_than_a_sword", MacheteTests::macheteSwingsFasterAndLighterThanASword);
    }

    /** The mineable/machete rule baked into the tool component: leaves, wool and cactus at the tier's speed, cobweb at a sword's. */
    private static void macheteCutsPlantsAtItsMaterialSpeed(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (MacheteItem item : MachetesItems.machetes()) {
            float speed = Math.max(item.getToolTier().getEfficiency(), 1.5F);
            ItemStack stack = new ItemStack(item);
            for (BlockState state : List.of(Blocks.OAK_LEAVES.defaultBlockState(), Blocks.WOOL.white().defaultBlockState(), Blocks.CACTUS.defaultBlockState(), Blocks.VINE.defaultBlockState())) {
                if (stack.getDestroySpeed(state) != speed) {
                    wrong.add(item + " cuts " + state.getBlock() + " at " + stack.getDestroySpeed(state) + " rather than " + speed);
                }
            }
            if (stack.getDestroySpeed(Blocks.COBWEB.defaultBlockState()) != 15.0F || !stack.isCorrectToolForDrops(Blocks.COBWEB.defaultBlockState())) {
                wrong.add(item + " does not cut cobweb like a sword");
            }
            if (stack.getDestroySpeed(Blocks.STONE.defaultBlockState()) != 1.0F) {
                wrong.add(item + " mines stone at " + stack.getDestroySpeed(Blocks.STONE.defaultBlockState()));
            }
            if (!stack.is(ItemTags.SWORDS) || !stack.is(LibCommonTags.Items.TOOLS_MELEE_WEAPONS) || !stack.is(ItemTags.SHARP_WEAPON_ENCHANTABLE)) {
                wrong.add(item + " is missing from the sword, melee weapon or sharp weapon tags");
            }
        }

        helper.assertTrue(MachetesItems.machetes().size() == 6 + ToolTiers.get().extras().size(), "expected one machete per vanilla tier and per extra material");
        helper.assertTrue(wrong.isEmpty(), String.join("; ", wrong));
        helper.succeed();
    }

    /** Against vanilla's sword of the same material: a lighter hit, a quicker swing and the same durability. */
    private static void macheteSwingsFasterAndLighterThanASword(GameTestHelper helper) {
        compare(helper, MachetesItems.WOOD_MACHETE.get(), Items.WOODEN_SWORD);
        compare(helper, MachetesItems.STONE_MACHETE.get(), Items.STONE_SWORD);
        compare(helper, MachetesItems.GOLD_MACHETE.get(), Items.GOLDEN_SWORD);
        compare(helper, MachetesItems.IRON_MACHETE.get(), Items.IRON_SWORD);
        compare(helper, MachetesItems.DIAMOND_MACHETE.get(), Items.DIAMOND_SWORD);
        compare(helper, MachetesItems.NETHERITE_MACHETE.get(), Items.NETHERITE_SWORD);

        helper.assertTrue(MachetesItems.NETHERITE_MACHETE.get().getDefaultInstance().has(DataComponents.DAMAGE_RESISTANT), "the netherite machete should survive fire and lava");
        helper.succeed();
    }

    private static void compare(GameTestHelper helper, Item machete, Item sword) {
        String name = machete.toString();
        double swordDamage = swing(sword.getDefaultInstance(), Attributes.ATTACK_DAMAGE);
        double macheteDamage = swing(machete.getDefaultInstance(), Attributes.ATTACK_DAMAGE);
        double swordSpeed = swing(sword.getDefaultInstance(), Attributes.ATTACK_SPEED);
        double macheteSpeed = swing(machete.getDefaultInstance(), Attributes.ATTACK_SPEED);

        helper.assertTrue(macheteDamage < swordDamage, name + " hits for " + macheteDamage + ", not less than the sword's " + swordDamage);
        helper.assertTrue(macheteSpeed > swordSpeed, name + " swings at " + macheteSpeed + ", not faster than the sword's " + swordSpeed);
        helper.assertValueEqual(machete.getDefaultInstance().getMaxDamage(), sword.getDefaultInstance().getMaxDamage(), name + " durability against its sword");
    }

    private static double swing(ItemStack stack, Holder<Attribute> attribute) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).compute(attribute, 0.0D, EquipmentSlot.MAINHAND);
    }
}
