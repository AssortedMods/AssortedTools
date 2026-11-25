package com.grim3212.assorted.shears.client.data;

import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ShearsItemModelProvider extends ItemModelProvider {

    public ShearsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Shears item models";
    }

    @Override
    protected void registerModels() {
        for (Item shears : ShearsItems.shears()) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(shears);
            this.withExistingParent(id.getPath(), "item/handheld").texture("layer0", id.withPrefix("item/"));
        }
    }
}
