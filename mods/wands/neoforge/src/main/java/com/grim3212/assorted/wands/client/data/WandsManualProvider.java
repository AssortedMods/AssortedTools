package com.grim3212.assorted.wands.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.Family;
import com.grim3212.assorted.wands.common.item.WandsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class WandsManualProvider extends LibManualProvider {

    public WandsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder wands = this.chapter("wands", 120);
        wands.text("modes");
        wands.recipes("basic", WandsItems.BUILDING_WAND.get(), WandsItems.MINING_WAND.get(), WandsItems.BREAKING_WAND.get()).every(60)
                .opensEveryItem(id -> id.getPath().endsWith("_wand") && !id.getPath().startsWith("reinforced_"));
        wands.recipes("reinforced", WandsItems.REINFORCED_BUILDING_WAND.get(), WandsItems.REINFORCED_MINING_WAND.get(), WandsItems.REINFORCED_BREAKING_WAND.get()).every(60)
                .opensEveryItem(id -> id.getPath().endsWith("_wand") && id.getPath().startsWith("reinforced_"));
    }
}
