package com.grim3212.assorted.portableworkbench.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.Family;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class PortableWorkbenchManualProvider extends LibManualProvider {

    public PortableWorkbenchManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        this.chapter("portable_workbench", 60).recipes("portable_workbench", PortableWorkbenchItems.PORTABLE_WORKBENCH.get()).opens(PortableWorkbenchItems.PORTABLE_WORKBENCH.get());
    }
}
