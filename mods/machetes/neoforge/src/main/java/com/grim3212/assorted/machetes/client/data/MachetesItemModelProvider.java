package com.grim3212.assorted.machetes.client.data;

import com.grim3212.assorted.machetes.Constants;
import com.grim3212.assorted.machetes.common.item.MachetesItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MachetesItemModelProvider extends ItemModelProvider {

    public MachetesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Machetes item models";
    }

    @Override
    protected void registerModels() {
        for (Item item : MachetesItems.machetes()) {
            this.handheldItem(item);
        }
    }
}
