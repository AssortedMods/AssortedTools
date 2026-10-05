package com.grim3212.assorted.multitools.client.data;

import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.common.item.MultitoolsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class MultitoolsItemModelProvider extends ModelProvider {

    public MultitoolsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Multitools item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (Item multitool : MultitoolsItems.multitools()) {
            itemModels.generateFlatItem(multitool, ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }
}
