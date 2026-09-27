package com.grim3212.assorted.staffs.common.item;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.Family;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class StaffsDataComponents {

    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENTS = RegistryProvider.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<DataComponentType<StaffModeInfo>> STAFF_MODE_INFO = DATA_COMPONENTS.register("staff_mode_info",
            () -> new DataComponentType.Builder<StaffModeInfo>().persistent(StaffModeInfo.CODEC).networkSynchronized(StaffModeInfo.STREAM_CODEC).build());

    // Runs before StaffsItems, whose staffs carry this as a default component.
    public static void init() {
        Services.PLATFORM.showComponentTooltip(STAFF_MODE_INFO);
    }
}
