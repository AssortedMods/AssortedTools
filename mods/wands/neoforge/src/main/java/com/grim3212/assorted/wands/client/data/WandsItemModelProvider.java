package com.grim3212.assorted.wands.client.data;

import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.common.item.WandsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WandsItemModelProvider extends ItemModelProvider {

    public WandsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Wands item models";
    }

    @Override
    protected void registerModels() {
        for (Item wand : WandsItems.wands()) {
            this.handheldItem(wand);
        }
    }
}
