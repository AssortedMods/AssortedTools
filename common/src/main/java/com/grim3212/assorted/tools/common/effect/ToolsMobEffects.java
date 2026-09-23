package com.grim3212.assorted.tools.common.effect;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ToolsMobEffects {

    public static final RegistryProvider<MobEffect> MOB_EFFECTS = RegistryProvider.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    /**
     * A marker, carrying no modifiers of its own: there is no attribute behind lava movement, so
     * {@code LivingEntityLavaSwimMixin} is what reads this.
     */
    public static final IRegistryObject<MobEffect> LAVA_STRIDING = MOB_EFFECTS.register("lava_striding",
            () -> new LavaStridingEffect(MobEffectCategory.BENEFICIAL, 0xD1541F));

    public static void init() {
    }
}
