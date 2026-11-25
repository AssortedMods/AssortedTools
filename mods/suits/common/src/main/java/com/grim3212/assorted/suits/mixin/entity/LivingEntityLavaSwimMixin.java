package com.grim3212.assorted.suits.mixin.entity;

import com.grim3212.assorted.suits.common.effect.SuitsMobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * A mixin because the lava branch of {@code travel} hardcodes its drag with no attribute or effect to
 * read; the drag is the whole difference between water (0.8 kept per tick) and lava (0.5). The slice
 * keeps it to that branch, which runs from the {@code isInLava} check to the elytra's {@code isFallFlying}.
 */
@Mixin(LivingEntity.class)
public class LivingEntityLavaSwimMixin {

    /** {@code LivingEntity#getWaterSlowDown}. */
    private static final double WATER_DRAG = 0.8D;

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.5D),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z")))
    private double assortedtools_lavaSwimsLikeWater(double lavaDrag) {
        LivingEntity self = (LivingEntity) (Object) this;
        return self.hasEffect(SuitsMobEffects.LAVA_STRIDING.asHolder()) ? WATER_DRAG : lavaDrag;
    }
}
