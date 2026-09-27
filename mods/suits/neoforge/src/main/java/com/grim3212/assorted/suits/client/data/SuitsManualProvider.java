package com.grim3212.assorted.suits.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.Family;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class SuitsManualProvider extends LibManualProvider {

    public SuitsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder suits = this.chapter("suits", 100);
        suits.recipes("chicken_suit", SuitsItems.CHICKEN_SUIT_HELMET.get(), SuitsItems.CHICKEN_SUIT_CHESTPLATE.get(), SuitsItems.CHICKEN_SUIT_LEGGINGS.get(), SuitsItems.CHICKEN_SUIT_BOOTS.get()).every(50)
                .opensEveryItem(id -> id.getPath().startsWith("chicken_suit_"));
        suits.recipes("scuba_suit", SuitsItems.SCUBA_HELMET.get(), SuitsItems.SCUBA_CHESTPLATE.get(), SuitsItems.SCUBA_LEGGINGS.get(), SuitsItems.SCUBA_BOOTS.get()).every(50)
                .opensEveryItem(id -> id.getPath().startsWith("scuba_"));
        suits.recipes("lava_suit", SuitsItems.LAVA_HELMET.get(), SuitsItems.LAVA_CHESTPLATE.get(), SuitsItems.LAVA_LEGGINGS.get(), SuitsItems.LAVA_BOOTS.get()).every(50)
                .opensEveryItem(id -> id.getPath().startsWith("lava_"));
    }
}
