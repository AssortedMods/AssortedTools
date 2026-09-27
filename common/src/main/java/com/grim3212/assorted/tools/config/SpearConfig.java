package com.grim3212.assorted.tools.config;

import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.tools.api.item.SpearStats;

import java.util.function.Supplier;

/** An extra material's spear lunge values, which vanilla tunes per material. Baked in at registration, so changes need a restart. */
public class SpearConfig {

    // In the order Item.Properties#spear takes them.
    public final Supplier<Double> swingSeconds;
    public final Supplier<Double> damageMultiplier;
    public final Supplier<Double> delaySeconds;
    public final Supplier<Double> dismountSeconds;
    public final Supplier<Double> dismountSpeed;
    public final Supplier<Double> knockbackSeconds;
    public final Supplier<Double> knockbackSpeed;
    public final Supplier<Double> damageSeconds;
    public final Supplier<Double> damageSpeed;

    /** Defaults are vanilla's values for the vanilla material of the same harvest level. */
    public SpearConfig(IConfigurationBuilder builder, String path, String name, int harvestLevel) {
        SpearStats defaults = SpearStats.forHarvestLevel(harvestLevel);
        String prefix = path + "." + name + ".";
        this.swingSeconds = builder.defineDouble(prefix + "swingSeconds", defaults.swingSeconds(), 0F, 100F, "How long this material's spear takes to stab, in seconds. Vanilla: wood 0.65, stone 0.75, iron 0.95, diamond 1.05, netherite 1.15.");
        this.damageMultiplier = builder.defineDouble(prefix + "damageMultiplier", defaults.damageMultiplier(), 0F, 100F, "The multiplier on the damage a lunge with this material's spear does. Vanilla: wood 0.7, stone 0.82, iron 0.95, diamond 1.075, netherite 1.2.");
        this.delaySeconds = builder.defineDouble(prefix + "delaySeconds", defaults.delaySeconds(), 0F, 100F, "The delay before a lunge with this material's spear lands, in seconds. Vanilla: wood 0.75, stone 0.7, iron 0.6, diamond 0.5, netherite 0.4.");
        this.dismountSeconds = builder.defineDouble(prefix + "dismountSeconds", defaults.dismountSeconds(), 0F, 100F, "How long a lunge has to be charged before it can dismount a rider, in seconds. Vanilla: wood 5.0, stone 4.5, iron 2.5, diamond 3.0, netherite 2.5.");
        this.dismountSpeed = builder.defineDouble(prefix + "dismountSpeed", defaults.dismountSpeed(), 0F, 1000F, "How fast the attacker has to be moving for a lunge to dismount a rider. Vanilla: wood 14.0, stone 13.0, iron 11.0, diamond 10.0, netherite 9.0.");
        this.knockbackSeconds = builder.defineDouble(prefix + "knockbackSeconds", defaults.knockbackSeconds(), 0F, 100F, "How long a lunge has to be charged before it knocks back, in seconds. Vanilla: wood 10.0, stone 9.0, iron 6.75, diamond 6.5, netherite 5.5.");
        this.knockbackSpeed = builder.defineDouble(prefix + "knockbackSpeed", defaults.knockbackSpeed(), 0F, 1000F, "How fast the attacker has to be moving for a lunge to knock back. Vanilla: 5.1 for every material.");
        this.damageSeconds = builder.defineDouble(prefix + "damageSeconds", defaults.damageSeconds(), 0F, 100F, "How long a lunge has to be charged before it damages, in seconds. Vanilla: wood 15.0, stone 13.75, iron 11.25, diamond 10.0, netherite 8.75.");
        this.damageSpeed = builder.defineDouble(prefix + "damageSpeed", defaults.damageSpeed(), 0F, 1000F, "How fast the attacker has to be moving, relative to the target, for a lunge to damage. Vanilla: 4.6 for every material.");
    }

    public SpearStats getSpearStats() {
        return new SpearStats(this.swingSeconds.get(), this.damageMultiplier.get(), this.delaySeconds.get(),
                this.dismountSeconds.get(), this.dismountSpeed.get(),
                this.knockbackSeconds.get(), this.knockbackSpeed.get(),
                this.damageSeconds.get(), this.damageSpeed.get());
    }
}
