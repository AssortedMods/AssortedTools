package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class PokeballDataComponentTagProvider extends TagsProvider<DataComponentType<?>> {

    public PokeballDataComponentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DATA_COMPONENT_TYPE, lookup, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(PokeballDataComponents.CAGE_ENTITY_DATA).add(BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(PokeballDataComponents.CAPTURED_ENTITY.get()).orElseThrow());
    }
}
