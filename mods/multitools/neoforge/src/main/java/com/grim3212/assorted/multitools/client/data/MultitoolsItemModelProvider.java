package com.grim3212.assorted.multitools.client.data;

import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MultitoolsItemModelProvider extends ItemModelProvider {

    public MultitoolsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Multitools item models";
    }

    @Override
    protected void registerModels() {
        for (Item item : MultitoolsItems.multitools()) {
            this.handheldItem(item);
        }
    }
}
