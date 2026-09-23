package com.grim3212.assorted.tools.mixin.entity;

import com.grim3212.assorted.tools.common.item.LavaArmorItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The whole lava suit makes its wearer fire immune, not merely fire resistant, which stops the
 * damage but not the flames over the screen. {@code isOnFire} and every ignite call read this.
 */
@Mixin(Entity.class)
public class EntityFireImmuneMixin {

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void assortedtools_lavaSuitIsFireImmune(CallbackInfoReturnable<Boolean> callback) {
        if ((Object) this instanceof LivingEntity wearer && LavaArmorItem.isWholeSuit(wearer)) {
            callback.setReturnValue(true);
        }
    }
}
