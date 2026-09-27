package com.grim3212.assorted.ultimatefist.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.Family;
import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class UltimateFistManualProvider extends LibManualProvider {

    public UltimateFistManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder ultimate = this.chapter("ultimate", 140);
        // Fragments are chest loot only, so there is no recipe to draw.
        ultimate.items("fragments", UltimateFistItems.fragments().toArray(FragmentItem[]::new))
                .every(30).opensEveryItem(id -> id.getPath().endsWith("_fragment"));
        ultimate.recipes("fist", UltimateFistItems.ULTIMATE_FIST.get()).opens(UltimateFistItems.ULTIMATE_FIST.get());
    }
}
