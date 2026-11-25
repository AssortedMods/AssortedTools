package com.grim3212.assorted.pokeball.client.data;

import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PokeballItemModelProvider extends ItemModelProvider {

    public PokeballItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Pokeball item models";
    }

    @Override
    protected void registerModels() {
        this.basicItem(PokeballItems.POKEBALL.get());
    }
}
