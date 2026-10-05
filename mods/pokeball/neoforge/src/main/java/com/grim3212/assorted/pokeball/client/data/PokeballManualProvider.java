package com.grim3212.assorted.pokeball.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class PokeballManualProvider extends LibManualProvider {

    public PokeballManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("pokeball", 150).recipes("pokeball", PokeballItems.POKEBALL.get()).opens(PokeballItems.POKEBALL.get());
    }
}
