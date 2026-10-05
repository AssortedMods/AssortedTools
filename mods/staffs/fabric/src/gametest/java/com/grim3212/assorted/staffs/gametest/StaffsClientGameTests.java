package com.grim3212.assorted.staffs.gametest;

import com.grim3212.assorted.staffs.client.FabricFrozenClient;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.zombie.Zombie;

/**
 * What only the client can check: a mob frozen on the server arrives frozen and is drawn as ice. Run with
 * {@code ./gradlew :staffs:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class StaffsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            frozenMobsAreDrawnAsIce(context, world);
        }
    }

    /**
     * A mob the server freezes should reach this client frozen, with the render-state flag the ice
     * layer reads. Fabric only syncs an attachment type the client had registered before connecting.
     */
    private static void frozenMobsAreDrawnAsIce(ClientGameTestContext context, TestSingleplayerContext world) {
        int id = world.getServer().computeOnServer(server -> {
            ServerPlayer player = server.getPlayerList().getPlayers().getFirst();
            ServerLevel level = player.level();
            Zombie zombie = EntityTypes.ZOMBIE.create(level, EntitySpawnReason.COMMAND);
            zombie.setPos(player.getX() + 2, player.getY(), player.getZ());
            level.addFreshEntity(zombie);
            if (!FrozenMobs.freeze(zombie)) {
                throw new AssertionError("the server would not freeze a fresh zombie");
            }
            return zombie.getId();
        });

        context.waitFor(client -> client.level.getEntity(id) != null);
        try {
            context.waitFor(client -> FrozenMobs.isFrozen(client.level.getEntity(id)), 100);
        } catch (AssertionError e) {
            throw new AssertionError("a zombie frozen on the server never arrived frozen on the client", e);
        }

        context.runOnClient(client -> {
            Entity zombie = client.level.getEntity(id);
            EntityRenderState state = client.getEntityRenderDispatcher().extractEntity(zombie, 1.0F);
            if (!(state instanceof LivingEntityRenderState living) || !living.getDataOrDefault(FabricFrozenClient.FROZEN, false)) {
                throw new AssertionError("a frozen zombie's render state is not marked frozen, so no ice is drawn");
            }
        });
    }
}
