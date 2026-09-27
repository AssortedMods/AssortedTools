package com.grim3212.assorted.multitools.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.multitools.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MultitoolsCommonConfig {

    // Keyed by tier name.
    private final Map<String, Supplier<Double>> durabilityModifiers = new HashMap<>();

    public MultitoolsCommonConfig() {
        // Needed at registration: a multitool bakes its durability in as it is constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        ToolTiers tiers = ToolTiers.get();
        for (ToolTier tier : tiers.vanilla()) {
            define(builder, tier.getName());
        }
        tiers.extras().keySet().forEach(name -> define(builder, name));

        builder.setup();
    }

    public float durabilityModifier(ToolTier tier) {
        return this.durabilityModifiers.get(tier.getName()).get().floatValue();
    }

    private void define(IConfigurationBuilder builder, String name) {
        this.durabilityModifiers.put(name, builder.defineDouble("multitools." + name + ".durabilityModifier", 1.5F, 0F, 1000F, "The modifier that will be used to calculate the multitool maximum uses. Normal tool material maxUses * this modifier."));
    }
}
