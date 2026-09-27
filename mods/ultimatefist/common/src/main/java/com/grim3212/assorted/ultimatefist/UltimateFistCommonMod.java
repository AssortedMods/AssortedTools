package com.grim3212.assorted.ultimatefist;

import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.ultimatefist.common.handlers.FragmentLootHandler;
import com.grim3212.assorted.ultimatefist.common.handlers.UltimateFistCreativeItems;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import com.grim3212.assorted.ultimatefist.config.UltimateFistConfig;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class UltimateFistCommonMod {

    public static final UltimateFistConfig COMMON_CONFIG = new UltimateFistConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        UltimateFistItems.init();
        UltimateFistCreativeItems.init();

        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> FragmentLootHandler.init(event));

        // Recipes and chest loot from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
