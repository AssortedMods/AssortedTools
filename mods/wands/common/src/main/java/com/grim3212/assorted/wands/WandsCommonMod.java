package com.grim3212.assorted.wands;

import com.grim3212.assorted.lib.core.item.ModeSwitching;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.wands.common.handlers.WandsCreativeItems;
import com.grim3212.assorted.wands.common.item.WandsDataComponents;
import com.grim3212.assorted.wands.common.item.WandsItems;
import com.grim3212.assorted.wands.config.WandsCommonConfig;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class WandsCommonMod {

    public static final WandsCommonConfig COMMON_CONFIG = new WandsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        WandsDataComponents.init();
        WandsItems.init();
        WandsCreativeItems.init();
        ModeSwitching.enable();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
