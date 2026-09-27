package com.grim3212.assorted.pokeball;

import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.pokeball.common.entity.PokeballEntities;
import com.grim3212.assorted.pokeball.common.handlers.PokeballCreativeItems;
import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class PokeballCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        PokeballDataComponents.init();
        PokeballItems.init();
        PokeballEntities.init();
        PokeballCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
