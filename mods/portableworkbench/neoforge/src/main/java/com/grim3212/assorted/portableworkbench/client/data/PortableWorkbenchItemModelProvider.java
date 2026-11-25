package com.grim3212.assorted.portableworkbench.client.data;

import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PortableWorkbenchItemModelProvider extends ItemModelProvider {

    public PortableWorkbenchItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Portable Workbench item models";
    }

    @Override
    protected void registerModels() {
        this.basicItem(PortableWorkbenchItems.PORTABLE_WORKBENCH.get());
    }
}
