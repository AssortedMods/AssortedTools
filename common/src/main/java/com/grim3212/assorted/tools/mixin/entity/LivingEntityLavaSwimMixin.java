package com.grim3212.assorted.tools.mixin.entity;

import com.grim3212.assorted.tools.common.effect.ToolsMobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Lava swum like water while Lava Striding. A mixin because {@code travelInLava} hardcodes its
 * speed and drag with no attribute or effect to read, and the drag is the whole difference: water
 * keeps 0.8 of its movement a tick, lava 0.5.
 */
@Mixin(LivingEntity.class)
public class LivingEntityLavaSwimMixin {

    /** {@code LivingEntity#getWaterSlowDown}. */
    private static final double WATER_DRAG = 0.8D;

    @ModifyConstant(method = "travelInLava", constant = @Constant(doubleValue = 0.5D))
    private double assortedtools_lavaSwimsLikeWater(double lavaDrag) {
        LivingEntity self = (LivingEntity) (Object) this;
        return self.hasEffect(ToolsMobEffects.LAVA_STRIDING.asHolder()) ? WATER_DRAG : lavaDrag;
    }
}
