package com.grim3212.assorted.suits.mixin.entity;

import com.grim3212.assorted.suits.common.item.IWornTick;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Ticks the suit pieces a living entity wears; see {@link IWornTick}. */
@Mixin(LivingEntity.class)
public class LivingEntityWornSuitMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void assortedtools_tickWornSuit(CallbackInfo callback) {
        LivingEntity wearer = (LivingEntity) (Object) this;
        if (wearer.isRemoved() || !(wearer.level() instanceof ServerLevel level)) {
            return;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
                continue;
            }

            ItemStack stack = wearer.getItemBySlot(slot);
            if (stack.getItem() instanceof IWornTick worn) {
                worn.wornTick(stack, level, wearer, slot);
            }
        }
    }
}
