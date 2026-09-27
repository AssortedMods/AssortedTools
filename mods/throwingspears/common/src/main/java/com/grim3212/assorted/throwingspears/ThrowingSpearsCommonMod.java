package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.throwingspears.common.entity.ThrowingSpearsEntities;
import com.grim3212.assorted.throwingspears.common.handlers.ThrowingSpearsCreativeItems;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import com.grim3212.assorted.throwingspears.config.ThrowingSpearsCommonConfig;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class ThrowingSpearsCommonMod {

    public static final ThrowingSpearsCommonConfig COMMON_CONFIG = new ThrowingSpearsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        ThrowingSpearsEntities.init();
        ThrowingSpearsItems.init();
        ThrowingSpearsCreativeItems.init();

        // Recipes, enchantments and the spear damage type from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
