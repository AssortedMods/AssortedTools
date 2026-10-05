package com.grim3212.assorted.wands.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.wands.Constants;

import java.util.function.Supplier;

public class WandsCommonConfig {

    public final Supplier<Boolean> freeBuildMode;
    public final Supplier<Boolean> bedrockBreaking;
    public final Supplier<Boolean> easyMiningObsidian;

    public WandsCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        freeBuildMode = builder.defineBoolean("wands.freeBuildMode", false, "Set to true if you would like the wands to not require any blocks to build with.");
        bedrockBreaking = builder.defineBoolean("wands.bedrockBreaking", false, "Set to true if you would like the breaking wands to be able to break bedrock.");
        easyMiningObsidian = builder.defineBoolean("wands.easyMiningObsidian", false, "Set to true if you would like the mining wands to be able to mine obsidian.");

        builder.setup();
    }
}
