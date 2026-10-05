package com.grim3212.assorted.staffs;

import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import com.grim3212.assorted.staffs.platform.FabricFrozenStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class AssortedStaffsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StaffsCommonMod.init();
        FabricFrozenStorage.init();
        // Fabric has no per-entity tick event, so the level's entities are walked once a tick instead.
        ServerTickEvents.END_LEVEL_TICK.register(level -> level.getAllEntities().forEach(FrozenMobs::thawIfBurning));
    }
}
