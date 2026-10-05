package com.grim3212.assorted.multitools;

import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.multitools.common.handlers.MultitoolsCreativeItems;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import com.grim3212.assorted.multitools.config.MultitoolsCommonConfig;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class MultitoolsCommonMod {

    public static final MultitoolsCommonConfig COMMON_CONFIG = new MultitoolsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "diamond_multitool"), 130)
                .manualOrder(120);

        MultitoolsItems.init();
        MultitoolsCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
