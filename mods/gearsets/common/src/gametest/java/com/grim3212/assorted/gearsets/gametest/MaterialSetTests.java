package com.grim3212.assorted.gearsets.gametest;

import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.gearsets.gametest.GearSetsTestSupport.*;
import static com.grim3212.assorted.lib.test.TestSupport.stand;
import static com.grim3212.assorted.lib.test.TestSupport.survivalPlayer;

/** Every material's tool and armor set: crafting, equipping and protecting. */
final class MaterialSetTests {

    private MaterialSetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_material_tool_set_crafts", MaterialSetTests::everyMaterialToolSetCrafts);
        out.accept("every_material_armor_set_crafts", MaterialSetTests::everyMaterialArmorSetCrafts);
        out.accept("armor_equips_and_protects", MaterialSetTests::armorEquipsAndProtects);
        out.accept("each_tool_shape_swings_like_its_vanilla_shape", MaterialSetTests::eachToolShapeSwingsLikeItsVanillaShape);
    }

    /**
     * A material's tools rank against each other as vanilla's do. Everything numeric is baked into
     * {@code attribute_modifiers} at registration, so a wrong baseline is otherwise silent.
     */
    private static void eachToolShapeSwingsLikeItsVanillaShape(GameTestHelper helper) {
        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            String name = set.name();

            double swordDamage = swing(set.sword().get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double pickaxeDamage = swing(set.pickaxe().get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);
            double hoeDamage = swing(set.hoe().get().getDefaultInstance(), Attributes.ATTACK_DAMAGE);

            helper.assertTrue(pickaxeDamage < swordDamage, name + " pickaxe hits for " + pickaxeDamage + " against the sword's " + swordDamage + "; a pickaxe is not a sword");
            helper.assertTrue(hoeDamage < pickaxeDamage, name + " hoe hits for " + hoeDamage + " against the pickaxe's " + pickaxeDamage + "; the hoe is the worst weapon of the set");

            double swordSpeed = swing(set.sword().get().getDefaultInstance(), Attributes.ATTACK_SPEED);
            double pickaxeSpeed = swing(set.pickaxe().get().getDefaultInstance(), Attributes.ATTACK_SPEED);

            helper.assertTrue(pickaxeSpeed < swordSpeed, name + " pickaxe swings at " + pickaxeSpeed + ", the same as or faster than the sword's " + swordSpeed);
        }

        helper.succeed();
    }

    private static double swing(ItemStack stack, Holder<Attribute> attribute) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).compute(attribute, 0.0D, EquipmentSlot.MAINHAND);
    }

    /** A material whose tag is empty is skipped, since its recipes are conditioned on {@code item_tag_populated}. */
    private static void everyMaterialToolSetCrafts(GameTestHelper helper) {
        ItemStack rod = firstOf(LibCommonTags.Items.RODS_WOODEN);
        helper.assertFalse(rod.isEmpty(), "c:rods/wooden is empty, so no tool recipe could match");

        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            ItemStack x = firstOf(set.material());
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 3, List.of(x, x, x, no, rod, no, no, rod, no), set.pickaxe().get());
            assertCrafts(helper, 1, 3, List.of(x, rod, rod), set.shovel().get());
            assertCrafts(helper, 2, 3, List.of(x, x, x, rod, no, rod), set.axe().get());
            assertCrafts(helper, 2, 3, List.of(x, x, no, rod, no, rod), set.hoe().get());
            assertCrafts(helper, 1, 3, List.of(x, x, rod), set.sword().get());
            assertCrafts(helper, 3, 3, List.of(no, no, x, no, rod, no, rod, no, no), set.spear().get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    private static void everyMaterialArmorSetCrafts(GameTestHelper helper) {
        final ItemStack no = ItemStack.EMPTY;
        int families = 0;

        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            ItemStack x = firstOf(set.material());
            if (x.isEmpty()) {
                continue;
            }

            assertCrafts(helper, 3, 2, List.of(x, x, x, x, no, x), set.helmet().get());
            assertCrafts(helper, 3, 3, List.of(x, no, x, x, x, x, x, x, x), set.chestplate().get());
            assertCrafts(helper, 3, 3, List.of(x, x, x, x, no, x, x, no, x), set.leggings().get());
            assertCrafts(helper, 3, 2, List.of(x, no, x, x, no, x), set.boots().get());
            families++;
        }

        helper.assertTrue(families > 0, "not one material had a populated material tag, so nothing was checked");
        helper.succeed();
    }

    /** Armor grants its configured defense, which is baked into the {@code equippable} and {@code attribute_modifiers} components. */
    private static void armorEquipsAndProtects(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(4, 1, 4));

        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            ArmorMaterialConfig armor = set.armor();
            int expected = armor.getReductionAmounts().values().stream().mapToInt(Integer::intValue).sum();
            assertArmorValue(helper, player, set.name(), set.helmet().get(), set.chestplate().get(), set.leggings().get(), set.boots().get(), expected);
        }

        helper.succeed();
    }
}
