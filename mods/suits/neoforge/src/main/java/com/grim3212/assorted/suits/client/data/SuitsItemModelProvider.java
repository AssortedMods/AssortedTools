package com.grim3212.assorted.suits.client.data;

import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.common.item.SuitsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SuitsItemModelProvider extends ItemModelProvider {

    public SuitsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Suits item models";
    }

    @Override
    protected void registerModels() {
        for (Item piece : SuitsItems.suits()) {
            this.basicItem(piece);
        }
    }
}
