package com.grim3212.assorted.suits.common.effect;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SuitsMobEffects {

    public static final RegistryProvider<MobEffect> MOB_EFFECTS = RegistryProvider.create(Registries.MOB_EFFECT, Constants.MOD_ID).aliasFrom(Family.ID);

    /** A marker with no modifiers: no attribute governs lava movement, so {@code LivingEntityLavaSwimMixin} reads it. */
    public static final IRegistryObject<MobEffect> LAVA_STRIDING = MOB_EFFECTS.register("lava_striding",
            () -> new LavaStridingEffect(MobEffectCategory.BENEFICIAL, 0xD1541F));

    public static void init() {
    }
}
