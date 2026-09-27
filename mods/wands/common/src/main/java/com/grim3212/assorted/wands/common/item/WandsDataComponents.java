package com.grim3212.assorted.wands.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.Family;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class WandsDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<DataComponentType<WandModeInfo>> WAND_MODE_INFO = DATA_COMPONENTS.register("wand_mode_info",
            () -> new DataComponentType.Builder<WandModeInfo>().persistent(WandModeInfo.CODEC).networkSynchronized(WandModeInfo.STREAM_CODEC).build());

    // Runs before WandsItems, whose wands carry this as a default component.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(WAND_MODE_INFO);
    }
}
