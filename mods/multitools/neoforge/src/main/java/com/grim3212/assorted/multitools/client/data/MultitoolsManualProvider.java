package com.grim3212.assorted.multitools.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.Family;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class MultitoolsManualProvider extends LibManualProvider {

    public MultitoolsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        // Only vanilla materials' recipes: the extra ones need another mod's tools and may not load.
        this.chapter("multitools", 30).recipes("multitools", MultitoolsItems.IRON_MULTITOOL.get(), MultitoolsItems.DIAMOND_MULTITOOL.get(), MultitoolsItems.NETHERITE_MULTITOOL.get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_multitool"));
    }
}
