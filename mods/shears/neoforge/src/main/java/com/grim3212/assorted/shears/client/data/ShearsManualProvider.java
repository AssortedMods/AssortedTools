package com.grim3212.assorted.shears.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class ShearsManualProvider extends LibManualProvider {

    public ShearsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("shears", 50).recipesById("shears", recipeId(ShearsItems.WOOD_SHEARS.get()), recipeId("steel_shears"), recipeId(ShearsItems.NETHERITE_SHEARS.get()))
                .every(50).opensEveryItem(id -> id.getPath().endsWith("_shears"));
    }
}
