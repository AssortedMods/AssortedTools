package com.grim3212.assorted.boomerangs.client.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BoomerangsItemModelProvider extends ItemModelProvider {

    public BoomerangsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Boomerangs item models";
    }

    @Override
    protected void registerModels() {
        handheld(BoomerangsItems.WOOD_BOOMERANG.get());
        handheld(BoomerangsItems.DIAMOND_BOOMERANG.get());
    }

    private void handheld(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        this.withExistingParent(id.getPath(), "item/handheld").texture("layer0", id.withPrefix("item/"));
    }
}
