package com.grim3212.assorted.boomerangs.client.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.Family;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class BoomerangsManualProvider extends LibManualProvider {

    public BoomerangsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        this.chapter("boomerangs", 80).recipes("boomerangs", BoomerangsItems.WOOD_BOOMERANG.get(), BoomerangsItems.DIAMOND_BOOMERANG.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_boomerang"));
    }
}
