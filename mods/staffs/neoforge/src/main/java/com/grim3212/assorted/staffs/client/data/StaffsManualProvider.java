package com.grim3212.assorted.staffs.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class StaffsManualProvider extends LibManualProvider {

    public StaffsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        ChapterBuilder staffs = this.chapter("staffs", 130);
        staffs.recipes("neptune", StaffsItems.NEPTUNE_STAFF.get()).opens(StaffsItems.NEPTUNE_STAFF.get());
        staffs.recipes("phoenix", StaffsItems.PHOENIX_STAFF.get()).opens(StaffsItems.PHOENIX_STAFF.get());
        staffs.recipes("frost", StaffsItems.FROST_POWDER.get(), StaffsItems.ICE_CHARGE.get()).every(60).opens(StaffsItems.FROST_ROD.get(), StaffsItems.FROST_POWDER.get(), StaffsItems.ICE_CHARGE.get());
        staffs.recipes("power", StaffsItems.POWER_STAFF.get()).opens(StaffsItems.POWER_STAFF.get());
    }
}
