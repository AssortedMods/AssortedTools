package com.grim3212.assorted.boomerangs.gametest;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.lib.test.ClientChecks;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;

/** What only a client shows: everything drawn with its own art, and the manual pages that name it. */
public class BoomerangsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            ClientChecks.assertDrawsRight(context, Constants.MOD_ID);
        }
    }
}
