package com.grim3212.assorted.suits;

import com.grim3212.assorted.lib.events.AnvilUpdatedEvent;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.suits.common.effect.SuitsMobEffects;
import com.grim3212.assorted.suits.common.handlers.ChickenSuitConversionHandler;
import com.grim3212.assorted.suits.common.handlers.SuitsCreativeItems;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import com.grim3212.assorted.suits.common.network.SuitsPackets;
import com.grim3212.assorted.suits.config.SuitsConfig;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class SuitsCommonMod {

    public static final SuitsConfig COMMON_CONFIG = new SuitsConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        SuitsItems.init();
        SuitsMobEffects.init();
        SuitsPackets.init();
        SuitsCreativeItems.init();

        Services.EVENTS.registerEvent(AnvilUpdatedEvent.class, (final AnvilUpdatedEvent event) -> ChickenSuitConversionHandler.anvilUpdateEvent(event));

        // Recipes and enchantments from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
