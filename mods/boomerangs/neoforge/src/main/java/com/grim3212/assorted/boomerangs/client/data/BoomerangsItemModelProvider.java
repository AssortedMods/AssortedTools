package com.grim3212.assorted.boomerangs.client.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.common.item.BoomerangsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class BoomerangsItemModelProvider extends ModelProvider {

    public BoomerangsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Boomerangs item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(BoomerangsItems.WOOD_BOOMERANG.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(BoomerangsItems.DIAMOND_BOOMERANG.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
