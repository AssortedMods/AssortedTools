package com.grim3212.assorted.portableworkbench.client.data;

import com.grim3212.assorted.lib.data.LibManualProvider;
import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.data.PackOutput;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class PortableWorkbenchManualProvider extends LibManualProvider {

    public PortableWorkbenchManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        this.chapter("portable_workbench", 60).recipes("portable_workbench", PortableWorkbenchItems.PORTABLE_WORKBENCH.get()).opens(PortableWorkbenchItems.PORTABLE_WORKBENCH.get());
    }
}
