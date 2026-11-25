package com.grim3212.assorted.pokeball.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.pokeball.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class PokeballDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<DataComponentType<CapturedEntity>> CAPTURED_ENTITY = DATA_COMPONENTS.register("captured_entity",
            () -> new DataComponentType.Builder<CapturedEntity>().persistent(CapturedEntity.CODEC).networkSynchronized(CapturedEntity.STREAM_CODEC).build());

    /** Assorted Displays' cage shows the mob in any component in this tag, so a filled pokeball shows its mob without either mod needing the other. */
    public static final TagKey<DataComponentType<?>> CAGE_ENTITY_DATA = TagKey.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath("assorteddecor", "cage_entity_data"));

    // Runs before PokeballItems, whose pokeball carries this as a default component.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(CAPTURED_ENTITY);
    }
}
