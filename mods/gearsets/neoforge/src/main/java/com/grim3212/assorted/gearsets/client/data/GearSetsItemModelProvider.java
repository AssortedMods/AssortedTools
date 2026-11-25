package com.grim3212.assorted.gearsets.client.data;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.gearsets.common.item.GearSetsItems;
import com.grim3212.assorted.gearsets.common.item.MaterialSet;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GearSetsItemModelProvider extends ItemModelProvider {

    public GearSetsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Gear Sets item models";
    }

    @Override
    protected void registerModels() {
        for (MaterialSet set : GearSetsItems.MATERIALS.values()) {
            for (Item tool : new Item[]{set.sword().get(), set.pickaxe().get(), set.axe().get(), set.shovel().get(), set.hoe().get()}) {
                this.handheldItem(tool);
            }
            for (Item armor : new Item[]{set.helmet().get(), set.chestplate().get(), set.leggings().get(), set.boots().get()}) {
                this.basicItem(armor);
            }
        }
    }
}
