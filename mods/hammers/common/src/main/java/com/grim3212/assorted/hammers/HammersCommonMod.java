package com.grim3212.assorted.hammers;

import com.grim3212.assorted.hammers.common.handlers.HammersCreativeItems;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import com.grim3212.assorted.lib.migration.MovedIds;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class HammersCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        HammersItems.init();
        HammersCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
