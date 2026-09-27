package com.grim3212.assorted.buckets;

import com.grim3212.assorted.buckets.common.handlers.BucketsCreativeItems;
import com.grim3212.assorted.buckets.common.handlers.MilkingHandler;
import com.grim3212.assorted.buckets.common.item.BucketsDataComponents;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.buckets.config.BucketsConfig;
import com.grim3212.assorted.lib.events.EntityInteractEvent;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class BucketsCommonMod {

    public static final BucketsConfig CONFIG = new BucketsConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        BucketsDataComponents.init();
        BucketsItems.init();
        BucketsCreativeItems.init();

        Services.EVENTS.registerEvent(EntityInteractEvent.class, (final EntityInteractEvent event) -> MilkingHandler.interact(event));

        // Recipes from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
