package com.grim3212.assorted.pokeball.data;

import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class PokeballDataComponentTagProvider extends TagsProvider<DataComponentType<?>> {

    /** Assorted Displays' cage shows the mob in any component in this tag, so a filled pokeball shows its mob without either mod needing the other. */
    public static final TagKey<DataComponentType<?>> CAGE_ENTITY_DATA = TagKey.create(Registries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath("assorteddecor", "cage_entity_data"));

    public PokeballDataComponentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.DATA_COMPONENT_TYPE, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(CAGE_ENTITY_DATA).add(BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(PokeballDataComponents.CAPTURED_ENTITY.get()).orElseThrow());
    }
}
