package com.grim3212.assorted.ultimatefist.client.data;

import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class UltimateFistItemModelProvider extends ItemModelProvider {

    public UltimateFistItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Ultimate Fist item models";
    }

    @Override
    protected void registerModels() {
        this.handheldItem(UltimateFistItems.ULTIMATE_FIST.get());
        for (FragmentItem fragment : UltimateFistItems.fragments()) {
            this.basicItem(fragment);
        }
    }
}
