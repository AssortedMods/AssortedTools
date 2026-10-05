package com.grim3212.assorted.shears.client.data;

import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.common.item.ShearsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ShearsItemModelProvider extends ModelProvider {

    public ShearsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Shears item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (Item shears : ShearsItems.shears()) {
            itemModels.generateFlatItem(shears, ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }
}
