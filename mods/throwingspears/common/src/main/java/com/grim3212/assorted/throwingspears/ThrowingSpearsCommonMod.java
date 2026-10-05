package com.grim3212.assorted.throwingspears;

import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.throwingspears.common.entity.ThrowingSpearsEntities;
import com.grim3212.assorted.throwingspears.common.handlers.ThrowingSpearsCreativeItems;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import com.grim3212.assorted.throwingspears.config.ThrowingSpearsCommonConfig;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class ThrowingSpearsCommonMod {

    public static final ThrowingSpearsCommonConfig COMMON_CONFIG = new ThrowingSpearsCommonConfig();

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "diamond_throwing_spear"), 60)
                .manualOrder(120);

        ThrowingSpearsEntities.init();
        ThrowingSpearsItems.init();
        ThrowingSpearsCreativeItems.init();

        // Recipes, enchantments and the spear damage type from when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
