package com.grim3212.assorted.suits.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Armour that works while it is worn. 1.21.1 only ticks the items in a player's inventory, and without
 * telling it which slot, so {@code LivingEntityWornSuitMixin} calls this for every wearer, armour stands included.
 */
public interface IWornTick {

    /** Each server tick, for every armour slot this piece is worn in. */
    void wornTick(ItemStack stack, ServerLevel level, LivingEntity wearer, EquipmentSlot slot);
}
