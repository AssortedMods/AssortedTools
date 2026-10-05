package com.grim3212.assorted.pokeball;

import com.grim3212.assorted.lib.family.Families;
import com.grim3212.assorted.lib.migration.MovedIds;
import com.grim3212.assorted.pokeball.common.entity.PokeballEntities;
import com.grim3212.assorted.pokeball.common.handlers.PokeballCreativeItems;
import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.resources.Identifier;

/**
 * Loader-agnostic startup. Both loader entry points call {@link #init()} and nothing else; anything
 * a loader needs beyond it goes through AssortedLib's {@code Services}.
 */
public class PokeballCommonMod {

    public static void init() {
        Constants.LOG.info(Constants.MOD_NAME + " starting up...");
        Families.join(Constants.MOD_ID, Constants.FAMILY_ID)
                .icon(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "pokeball"), 10)
                .manualOrder(120);

        PokeballDataComponents.init();
        PokeballItems.init();
        PokeballEntities.init();
        PokeballCreativeItems.init();

        // Recipes unlocked when this was all one mod carry over to their new ids.
        MovedIds.inherit(Constants.FAMILY_ID, Constants.MOD_ID);
    }
}
