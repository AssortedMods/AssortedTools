package com.grim3212.assorted.staffs;

import com.grim3212.assorted.lib.core.item.ModeSwitching;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.staffs.common.entity.StaffsEntities;
import com.grim3212.assorted.staffs.common.handlers.StaffsCreativeItems;
import com.grim3212.assorted.staffs.common.handlers.StaffsLootHandlers;
import com.grim3212.assorted.staffs.common.item.StaffsDataComponents;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()}; the frozen-mob storage and the
 * tick that thaws burning mobs are each loader's own.
 */
public class StaffsCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "phoenix_staff"), 30)
                .manualOrder(120);

        StaffsDataComponents.init();
        StaffsEntities.init();
        StaffsItems.init();
        StaffsCreativeItems.init();
        ModeSwitching.enable();

        Services.EVENTS.registerEvent(LootTableModifyEvent.class, (final LootTableModifyEvent event) -> StaffsLootHandlers.init(event));

        // Recipes and loot tables from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
