package com.grim3212.assorted.suits.data;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.data.LibDatapackRegistryProvider;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/** Writes chicken jump as datapack JSON. Weight 5 and anvil cost 2 are the old uncommon rarity. */
public class SuitsEnchantmentData extends LibDatapackRegistryProvider {

    @Override
    public void addEntries(RegistrySetBuilder builder) {
        builder.add(Registries.ENCHANTMENT, SuitsEnchantmentData::bootstrap);
    }

    @Override
    public List<ResourceKey<? extends Registry<?>>> registries() {
        return Lists.newArrayList(Registries.ENCHANTMENT);
    }

    private static void bootstrap(BootstrapContext<Enchantment> context) {
        context.register(SuitsEnchantments.CHICKEN_JUMP, Enchantment.enchantment(
                Enchantment.definition(
                        context.lookup(Registries.ITEM).getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.constantCost(10),
                        Enchantment.constantCost(35),
                        2,
                        EquipmentSlotGroup.ARMOR)).build(SuitsEnchantments.CHICKEN_JUMP.identifier()));
    }
}
