package com.grim3212.assorted.extramaterials;

import com.grim3212.assorted.extramaterials.common.handlers.ExtraMaterialsCreativeItems;
import com.grim3212.assorted.extramaterials.common.item.ExtraMaterialsItems;
import com.grim3212.assorted.extramaterials.config.ExtraMaterialsConfig;
import com.grim3212.assorted.lib.migration.MovedIds;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class ExtraMaterialsCommonMod {

    public static final ExtraMaterialsConfig COMMON_CONFIG = new ExtraMaterialsConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        ExtraMaterialsItems.init();
        ExtraMaterialsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
