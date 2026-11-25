package com.grim3212.assorted.suits.common.handlers;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import com.grim3212.assorted.suits.common.item.ChickenSuitArmor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class ChickenSuitConversionHandler {

    public static void anvilUpdateEvent(AnvilUpdatedEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!(right.getItem() instanceof ChickenSuitArmor chickenArmor) || left.getItem() instanceof ChickenSuitArmor) {
            return;
        }

        // "Same armor type" is tested by comparing the slot each item is worn in.
        Equipable equipable = Equipable.get(left);
        if (equipable == null || !equipable.getEquipmentSlot().isArmor()) {
            return;
        }

        EquipmentSlot slot = equipable.getEquipmentSlot();
        if (slot != chickenArmor.getArmorType().getSlot()) {
            return;
        }

        // Enchantments are data, so it is looked up, and a datapack may have removed it.
        Optional<Holder.Reference<Enchantment>> chickenJumpHolder = event.getPlayer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(SuitsEnchantments.CHICKEN_JUMP);
        if (chickenJumpHolder.isEmpty() || !IItemEnchantmentCondition.supportedByDefault(left, chickenJumpHolder.get())) {
            return;
        }
        Holder<Enchantment> chickenJump = chickenJumpHolder.get();

        ItemStack output = left.copy();
        EnchantmentHelper.updateEnchantments(output, mutable -> mutable.set(chickenJump, 1));

        int cost = 0;
        if (!StringUtils.isBlank(event.getName()) && !event.getName().equals(output.getHoverName().getString())) {
            cost++;

            output.set(DataComponents.CUSTOM_NAME, Component.literal(event.getName()));
        }

        event.setOutput(output);
        event.setMaterialCost(1);

        switch (slot) {
            case HEAD -> event.setCost(cost + 2);
            case CHEST -> event.setCost(cost + 5);
            case LEGS -> event.setCost(cost + 4);
            case FEET -> event.setCost(cost + 2);
            default -> event.setCost(cost + 2);
        }
    }

}
