package com.grim3212.assorted.machetes.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.machetes.Constants;
import com.grim3212.assorted.machetes.common.item.MachetesItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class MachetesManualProvider extends LibManualProvider {

    public MachetesManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("machetes", 40).recipes("machetes", MachetesItems.WOOD_MACHETE.get(), MachetesItems.IRON_MACHETE.get(), MachetesItems.DIAMOND_MACHETE.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_machete"));
    }
}
