package com.grim3212.assorted.shears.data;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.data.LibDatapackRegistryProvider;
import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/** Writes Coral Cutter as datapack JSON, rare: weight 2 and anvil cost 4 on the old rarity scale. */
public class ShearsEnchantmentData extends LibDatapackRegistryProvider {

    @Override
    public void addEntries(RegistrySetBuilder builder) {
        builder.add(Registries.ENCHANTMENT, ShearsEnchantmentData::bootstrap);
    }

    @Override
    public List<ResourceKey<? extends Registry<?>>> registries() {
        return Lists.newArrayList(Registries.ENCHANTMENT);
    }

    private static void bootstrap(BootstrapContext<Enchantment> context) {
        context.register(ShearsEnchantments.CORAL_CUTTER, Enchantment.enchantment(
                Enchantment.definition(
                        context.lookup(Registries.ITEM).getOrThrow(ShearsTags.SHEARS_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.constantCost(12),
                        Enchantment.constantCost(52),
                        4,
                        EquipmentSlotGroup.MAINHAND)).build(ShearsEnchantments.CORAL_CUTTER.identifier()));
    }
}
