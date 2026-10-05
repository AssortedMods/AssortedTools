package com.grim3212.assorted.gearsets.gametest;

import com.grim3212.assorted.gearsets.GearSetsCommonMod;
import com.grim3212.assorted.gearsets.api.SpearStats;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.KineticWeapon;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** The vanilla-style spears of the extra materials. */
final class SpearTests {

    private SpearTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("material_spears_are_vanilla_spears", SpearTests::materialSpearsAreVanillaSpears);
    }

    /**
     * Every extra material's spear carries every component vanilla's iron spear does and sits in the same tags, so
     * vanilla's lunge, dismount and enchantments apply to it without a line of our own.
     */
    private static void materialSpearsAreVanillaSpears(GameTestHelper helper) {
        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            Item item = set.spear().get();
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();
            ItemStack stack = new ItemStack(item);

            for (TypedDataComponent<?> component : Items.IRON_SPEAR.components()) {
                helper.assertTrue(item.components().has(component.type()), name + " lacks the iron spear's " + component.type());
            }
            helper.assertTrue(stack.getOrDefault(DataComponents.DAMAGE_TYPE, null) != null && stack.get(DataComponents.DAMAGE_TYPE).is(DamageTypes.SPEAR), name + " does not deal spear damage");
            helper.assertValueEqual(stack.getMaxDamage(), set.tier().getMaxUses(), name + " durability, against its material's");

            SpearStats configured = GearSetsCommonMod.COMMON_CONFIG.spears.get(set.name()).getSpearStats();
            KineticWeapon lunge = stack.get(DataComponents.KINETIC_WEAPON);
            helper.assertValueEqual(lunge.damageMultiplier(), (float) configured.damageMultiplier(), name + " lunge damage multiplier, against its configuration");
            // Vanilla turns seconds into ticks in float arithmetic, so the expectation must too.
            helper.assertValueEqual(lunge.delayTicks(), (int) ((float) configured.delaySeconds() * 20.0F), name + " lunge delay, against its configuration");
            helper.assertTrue(stack.is(ItemTags.SPEARS), name + " is not in minecraft:spears");
            helper.assertTrue(stack.is(ItemTags.LUNGE_ENCHANTABLE), name + " cannot take lunge");
            helper.assertTrue(stack.is(ItemTags.MELEE_WEAPON_ENCHANTABLE), name + " cannot take melee enchantments");
            helper.assertTrue(stack.is(ItemTags.PIGLIN_PREFERRED_WEAPONS), name + " is not a weapon piglins prefer");
        }

        helper.succeed();
    }
}
