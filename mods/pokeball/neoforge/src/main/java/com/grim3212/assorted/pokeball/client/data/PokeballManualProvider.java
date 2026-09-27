package com.grim3212.assorted.pokeball.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.Family;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class PokeballManualProvider extends LibManualProvider {

    public PokeballManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        this.chapter("pokeball", 150).recipes("pokeball", PokeballItems.POKEBALL.get()).opens(PokeballItems.POKEBALL.get());
    }
}
