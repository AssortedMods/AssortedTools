package com.grim3212.assorted.tools.common.item.configurable;

/**
 * The attack damage and speed baselines the mod's tools are built with. Each shape uses vanilla's
 * own baseline for it, so an extra material's tool sits beside the vanilla tool of its tier rather
 * than out-damaging it. The hoe is the exception: vanilla picks a different pair per material, so
 * its damage follows the harvest level negated.
 */
public final class ConfigurableTools {

    public static final float PICKAXE_DAMAGE = 1.0F;
    public static final float PICKAXE_SPEED = -2.8F;
    public static final float SHOVEL_DAMAGE = 1.5F;
    public static final float SHOVEL_SPEED = -3.0F;
    public static final float SWORD_DAMAGE = 3.0F;
    public static final float SWORD_SPEED = -2.4F;
    public static final float HOE_SPEED = -0.0F;

    private ConfigurableTools() {
    }
}
