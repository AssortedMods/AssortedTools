package com.grim3212.assorted.throwingspears.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class ThrowingSpearsManualProvider extends LibManualProvider {

    public ThrowingSpearsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("throwing_spears", 70).recipes("throwing_spears", ThrowingSpearsItems.WOOD_THROWING_SPEAR.get(), ThrowingSpearsItems.IRON_THROWING_SPEAR.get(), ThrowingSpearsItems.DIAMOND_THROWING_SPEAR.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_throwing_spear"));
    }
}
