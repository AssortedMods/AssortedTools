package com.grim3212.assorted.throwingspears.config;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.throwingspears.Constants;

import java.util.List;
import java.util.function.Supplier;

public class ThrowingSpearsCommonConfig {
    public final Supplier<List<? extends Float>> conductivityLightningChances;

    public ThrowingSpearsCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        conductivityLightningChances = builder.defineList("better_spears.conductivityLightningChances", Lists.newArrayList(0.6F, 0.3F, 0.1F), Float.class, "The chances modifier for lightning to spawn at each level of conductivity. The smaller the number the higher chance.");

        builder.setup();
    }
}
