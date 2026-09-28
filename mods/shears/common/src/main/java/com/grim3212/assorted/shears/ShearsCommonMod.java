package com.grim3212.assorted.shears;

import com.grim3212.assorted.lib.events.CorrectToolForDropEvent;
import com.grim3212.assorted.lib.events.OnDropStacksEvent;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.shears.common.handlers.CoralCutterHandler;
import com.grim3212.assorted.shears.common.handlers.ShearsCreativeItems;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class ShearsCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "diamond_shears"), 80)
                .manualOrder(120);

        ShearsItems.init();
        ShearsCreativeItems.init();

        Services.EVENTS.registerEvent(OnDropStacksEvent.class, (final OnDropStacksEvent event) -> CoralCutterHandler.handleDrop(event));
        Services.EVENTS.registerEvent(CorrectToolForDropEvent.class, (final CorrectToolForDropEvent event) -> CoralCutterHandler.handleCorrectTool(event));

        // Recipes and enchantments from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
