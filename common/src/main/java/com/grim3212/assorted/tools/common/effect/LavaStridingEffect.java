package com.grim3212.assorted.tools.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** {@link MobEffect}'s constructor is protected, so an effect of one's own needs a class of one's own. */
public class LavaStridingEffect extends MobEffect {

    public LavaStridingEffect(MobEffectCategory category, int colour) {
        super(category, colour);
    }
}
