package com.grim3212.assorted.staffs.client.data;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.PowerStaffItem;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class StaffsItemModelProvider extends ItemModelProvider {

    public StaffsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Staffs item models";
    }

    @Override
    protected void registerModels() {
        this.handheldItem(StaffsItems.NEPTUNE_STAFF.get());
        this.handheldItem(StaffsItems.PHOENIX_STAFF.get());
        this.handheldItem(StaffsItems.FROST_ROD.get());
        this.basicItem(StaffsItems.FROST_POWDER.get());
        this.basicItem(StaffsItems.ICE_CHARGE.get());
        this.powerStaff(StaffsItems.POWER_STAFF.get());
    }

    /** Push and pull are two textures, the pull one picked by the {@code custom_model_data} the staff sets for its mode. */
    private void powerStaff(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        ItemModelBuilder pull = this.handheld(name + "_pull", name + "_pull");
        this.handheld(name, name + "_push").override()
                .predicate(ResourceLocation.withDefaultNamespace("custom_model_data"), PowerStaffItem.PULL_MODEL)
                .model(pull).end();
    }

    private ItemModelBuilder handheld(String model, String texture) {
        return this.withExistingParent(model, ResourceLocation.withDefaultNamespace("item/handheld")).texture("layer0", this.modLoc("item/" + texture));
    }
}
