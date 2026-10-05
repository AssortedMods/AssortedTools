package com.grim3212.assorted.hammers.client.data;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class HammersManualProvider extends LibManualProvider {

    public HammersManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("hammers", 20).recipes("hammers", HammersItems.WOOD_HAMMER.get(), HammersItems.IRON_HAMMER.get(), HammersItems.NETHERITE_HAMMER.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_hammer"));
    }
}
