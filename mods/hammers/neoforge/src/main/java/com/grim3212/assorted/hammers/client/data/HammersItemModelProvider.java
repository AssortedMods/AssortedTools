package com.grim3212.assorted.hammers.client.data;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.hammers.common.item.HammersItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class HammersItemModelProvider extends ItemModelProvider {

    public HammersItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Hammers item models";
    }

    @Override
    protected void registerModels() {
        for (Item item : HammersItems.hammers()) {
            this.handheldItem(item);
        }
    }
}
