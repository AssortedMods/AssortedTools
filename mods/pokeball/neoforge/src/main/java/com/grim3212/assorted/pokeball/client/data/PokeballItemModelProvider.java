package com.grim3212.assorted.pokeball.client.data;

import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class PokeballItemModelProvider extends ModelProvider {

    public PokeballItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Pokeball item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(PokeballItems.POKEBALL.get(), ModelTemplates.FLAT_ITEM);
    }
}
