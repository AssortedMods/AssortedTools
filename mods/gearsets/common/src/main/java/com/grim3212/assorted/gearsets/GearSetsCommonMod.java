package com.grim3212.assorted.gearsets;

import com.grim3212.assorted.gearsets.common.handlers.GearSetsCreativeItems;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.config.GearSetsConfig;
import com.grim3212.assorted.lib.migration.MovedIds;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class GearSetsCommonMod {

    public static final GearSetsConfig COMMON_CONFIG = new GearSetsConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        GearSetsItems.init();
        GearSetsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
