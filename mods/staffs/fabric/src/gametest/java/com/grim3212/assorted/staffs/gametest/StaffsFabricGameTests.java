package com.grim3212.assorted.staffs.gametest;

import com.grim3212.assorted.staffs.Constants;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/**
 * Registers this mod's test functions on Fabric. The gametest source set is a dev-only second mod,
 * so nothing in {@code main} references it and the tests never ship.
 */
public class StaffsFabricGameTests implements ModInitializer {

    @Override
    public void onInitialize() {
        StaffsGameTests.forEach((name, function) ->
                Registry.register(BuiltInRegistries.TEST_FUNCTION, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), function));
    }
}
