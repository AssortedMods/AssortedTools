package com.grim3212.assorted.suits.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.suits.Constants;

import java.util.function.Supplier;

public class SuitsClientConfig {
    public final Supplier<Boolean> chickenSuitCluck;

    public SuitsClientConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.CLIENT_ONLY, Constants.MOD_ID + "-client");

        chickenSuitCluck = builder.defineBoolean("chicken_suit.cluck", true, "Set this to false to stop the chicken cluck that plays on every extra jump in the chicken suit.");

        builder.setup();
    }
}
