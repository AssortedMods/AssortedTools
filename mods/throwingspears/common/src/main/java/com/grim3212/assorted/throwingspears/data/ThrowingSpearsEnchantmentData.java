package com.grim3212.assorted.throwingspears.data;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.data.LibDatapackRegistryProvider;
import com.grim3212.assorted.throwingspears.common.enchantment.ThrowingSpearsEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/**
 * Writes the throwing spears' enchantments as datapack JSON. Weights and anvil costs follow the old rarity
 * scale: common, uncommon, rare, very rare are weight 10/5/2/1 and anvil cost 1/2/4/8.
 */
public class ThrowingSpearsEnchantmentData extends LibDatapackRegistryProvider {

    @Override
    public void addEntries(RegistrySetBuilder builder) {
        builder.add(Registries.ENCHANTMENT, ThrowingSpearsEnchantmentData::bootstrap);
    }

    @Override
    public List<ResourceKey<? extends Registry<?>>> registries() {
        return Lists.newArrayList(Registries.ENCHANTMENT);
    }

    private static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        HolderSet<Item> spears = items.getOrThrow(ThrowingSpearsEnchantments.SPEAR_ENCHANTABLE);

        HolderSet<Enchantment> unstable = HolderSet.direct(List.of(enchantments.getOrThrow(ThrowingSpearsEnchantments.UNSTABLE)));
        HolderSet<Enchantment> unstableExclusions = HolderSet.direct(List.of(
                enchantments.getOrThrow(ThrowingSpearsEnchantments.CONDUCTIVE),
                enchantments.getOrThrow(ThrowingSpearsEnchantments.FLAMMABLE),
                enchantments.getOrThrow(ThrowingSpearsEnchantments.BOUNCINESS)));

        register(context, ThrowingSpearsEnchantments.BOUNCINESS, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        5,
                        5,
                        Enchantment.dynamicCost(1, 8),
                        Enchantment.dynamicCost(36, 10),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ThrowingSpearsEnchantments.CONDUCTIVE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        1,
                        3,
                        Enchantment.dynamicCost(15, 9),
                        Enchantment.dynamicCost(61, 10),
                        8,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ThrowingSpearsEnchantments.FLAMMABLE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        5,
                        1,
                        Enchantment.constantCost(5),
                        Enchantment.constantCost(30),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstable));

        register(context, ThrowingSpearsEnchantments.UNSTABLE, Enchantment.enchantment(
                Enchantment.definition(
                        spears,
                        2,
                        2,
                        Enchantment.dynamicCost(10, 8),
                        Enchantment.dynamicCost(51, 10),
                        4,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(unstableExclusions));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}
