package com.grim3212.assorted.machetes;

import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.machetes.common.handlers.MachetesCreativeItems;
import com.grim3212.assorted.machetes.common.item.MachetesItems;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class MachetesCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        MachetesItems.init();
        MachetesCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
