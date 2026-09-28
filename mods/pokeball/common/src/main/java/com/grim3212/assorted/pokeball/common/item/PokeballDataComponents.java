package com.grim3212.assorted.pokeball.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.pokeball.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class PokeballDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<DataComponentType<CapturedEntity>> CAPTURED_ENTITY = DATA_COMPONENTS.register("captured_entity",
            () -> new DataComponentType.Builder<CapturedEntity>().persistent(CapturedEntity.CODEC).networkSynchronized(CapturedEntity.STREAM_CODEC).build());

    // Runs before PokeballItems, whose pokeball carries this as a default component.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(CAPTURED_ENTITY);
    }
}
