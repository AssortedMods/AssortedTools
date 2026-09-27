package com.grim3212.assorted.ultimatefist.client.data;

import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class UltimateFistItemModelProvider extends ModelProvider {

    public UltimateFistItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Ultimate Fist item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(UltimateFistItems.ULTIMATE_FIST.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        for (FragmentItem fragment : UltimateFistItems.fragments()) {
            itemModels.generateFlatItem(fragment, ModelTemplates.FLAT_ITEM);
        }
    }
}
