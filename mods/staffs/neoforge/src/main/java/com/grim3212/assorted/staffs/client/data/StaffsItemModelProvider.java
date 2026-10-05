package com.grim3212.assorted.staffs.client.data;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.PowerStaffItem;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.properties.select.CustomModelDataProperty;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class StaffsItemModelProvider extends ModelProvider {

    public StaffsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Staffs item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(StaffsItems.NEPTUNE_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(StaffsItems.PHOENIX_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(StaffsItems.FROST_ROD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(StaffsItems.FROST_POWDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(StaffsItems.ICE_CHARGE.get(), ModelTemplates.FLAT_ITEM);
        powerStaff(itemModels, StaffsItems.POWER_STAFF.get());
    }

    /** Push and pull are two textures, picked by the {@code custom_model_data} string the staff sets for its mode. */
    private void powerStaff(ItemModelGenerators itemModels, Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        Identifier push = ModelTemplates.FLAT_HANDHELD_ITEM.create(modelId(name), TextureMapping.layer0(texture(name + "_push")), itemModels.modelOutput);
        Identifier pull = ModelTemplates.FLAT_HANDHELD_ITEM.create(modelId(name + "_pull"), TextureMapping.layer0(texture(name + "_pull")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.select(new CustomModelDataProperty(0), ItemModelUtils.plainModel(push), ItemModelUtils.when(PowerStaffItem.PULL_MODEL, ItemModelUtils.plainModel(pull))));
    }

    private static Identifier modelId(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path);
    }

    private static Material texture(String name) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name));
    }
}
