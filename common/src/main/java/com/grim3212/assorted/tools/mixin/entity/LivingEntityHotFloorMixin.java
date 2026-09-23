package com.grim3212.assorted.tools.mixin.entity;

import com.grim3212.assorted.tools.common.item.LavaArmorItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Magma and campfires only ever burn what is stood in them, so the lava boots see them off on their
 * own. The whole suit is {@code EntityFireImmuneMixin}'s business, and covers these as well.
 */
@Mixin(LivingEntity.class)
public class LivingEntityHotFloorMixin {

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void assortedtools_lavaBootsTakeTheHeat(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> callback) {
        if (!source.is(DamageTypes.HOT_FLOOR) && !source.is(DamageTypes.CAMPFIRE)) {
            return;
        }

        if (LavaArmorItem.hasLavaBoots((LivingEntity) (Object) this)) {
            callback.setReturnValue(true);
        }
    }
}
