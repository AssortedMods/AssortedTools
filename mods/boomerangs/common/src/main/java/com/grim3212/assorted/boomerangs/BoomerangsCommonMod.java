package com.grim3212.assorted.boomerangs;

import com.grim3212.assorted.boomerangs.common.entity.BoomerangsEntities;
import com.grim3212.assorted.boomerangs.common.handlers.BoomerangsCreativeItems;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.boomerangs.config.BoomerangsCommonConfig;
import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class BoomerangsCommonMod {

    public static final BoomerangsCommonConfig COMMON_CONFIG = new BoomerangsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "diamond_boomerang"), 110)
                .manualOrder(120);

        BoomerangsEntities.init();
        BoomerangsItems.init();
        BoomerangsCreativeItems.init();

        // Recipes and the boomerang damage type from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
