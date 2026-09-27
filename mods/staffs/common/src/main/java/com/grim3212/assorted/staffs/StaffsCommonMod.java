package com.grim3212.assorted.staffs;

import com.grim3212.assorted.lib.core.item.ModeSwitching;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.staffs.common.entity.StaffsEntities;
import com.grim3212.assorted.staffs.common.handlers.StaffsCreativeItems;
import com.grim3212.assorted.staffs.common.handlers.StaffsLootHandlers;
import com.grim3212.assorted.staffs.common.item.StaffsDataComponents;
import com.grim3212.assorted.staffs.common.item.StaffsItems;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()}; the frozen-mob storage and the
 * tick that thaws burning mobs are each loader's own.
 */
public class StaffsCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");

        StaffsDataComponents.init();
        StaffsEntities.init();
        StaffsItems.init();
        StaffsCreativeItems.init();
        ModeSwitching.enable();

        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> StaffsLootHandlers.init(event));

        // Recipes and loot tables from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Family.ID, Constants.MOD_ID);
    }
}
