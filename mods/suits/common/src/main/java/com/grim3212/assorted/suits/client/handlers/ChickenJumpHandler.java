package com.grim3212.assorted.suits.client.handlers;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.suits.client.SuitsClient;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import com.grim3212.assorted.suits.common.item.ChickenSuitArmor;
import com.grim3212.assorted.suits.common.network.ChickenSuitUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ChickenJumpHandler {

    // Should be client side only so each client should have own instance of numJumps
    private static int numJumps;
    private static int cooldown = 0;
    private static final int MAX_COOLDOWN = 6;

    public static void tick(Minecraft mc) {
        Screen screen = mc.screen;
        if (screen == null) {
            onTickInGame(mc);
        }
    }

    private static void onTickInGame(Minecraft mc) {
        if (cooldown > 0) {
            --cooldown;
        }

        if (mc.player.onGround()) {
            numJumps = 0;
        }


        // hasImpulse only turns true on the client at a jump, so a player who hasn't jumped since joining or respawning
        // would never glide; being off the ground counts as well.
        if (!mc.player.isInWater() && !mc.player.isInLava() && (mc.player.hasImpulse || !mc.player.onGround())) {
            int jumpsAllowed = getMaxJumps(mc.player);

            // Must at least have 1 piece of the suit
            if (jumpsAllowed > 1) {
                boolean jumpPressed = mc.options.keyJump.isDown();

                if (jumpPressed && cooldown == 0 && numJumps < jumpsAllowed) {
                    if (numJumps > 0) {
                        mc.player.jumpFromGround();
                        mc.player.fallDistance = 0.0f;

                        cluck(mc);

                        // Double jump on server
                        Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket());
                    } else {
                        // Resets fall damage from flapping mid-air, not a jump off the ground.
                        if (mc.player.fallDistance > 0.1f) {
                            // If we jumped while in mid-air we still only get the amount of jumps equal to armor pieces with the enchant
                            numJumps++;

                            mc.player.jumpFromGround();
                            mc.player.fallDistance = -numJumps;

                            cluck(mc);

                            // Double jump on server
                            Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket());
                        }
                    }

                    numJumps++;
                    cooldown = MAX_COOLDOWN;
                }

                // Handle the glide
                Vec3 mot = mc.player.getDeltaMovement();
                if (!mc.options.keyShift.isDown() && mot.y < 0.0D) {
                    double d = -0.14999999999999999D - 0.14999999999999999D * (1.0D - (double) numJumps / 5D);
                    if (mot.y < d) {
                        mc.player.setDeltaMovement(mot.x, d, mot.z);
                    }
                    mc.player.fallDistance = 0.0F;

                    // Glide on server
                    Services.NETWORK.sendToServer(new ChickenSuitUpdatePacket(numJumps));
                }

            }
        }
    }

    /** Only the jumping player hears it, and only while their own client config leaves it on. */
    private static void cluck(Minecraft mc) {
        if (SuitsClient.CLIENT_CONFIG.chickenSuitCluck.get()) {
            mc.level.playSound(mc.player, mc.player.blockPosition(), SoundEvents.CHICKEN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static int getMaxJumps(Player player) {
        // Start at one for original jump
        int maxJumps = 1;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;

            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ChickenSuitArmor) {
                maxJumps++;
            } else if (SuitsEnchantments.hasChickenJump(stack)) {
                maxJumps++;
            }
        }

        return maxJumps;
    }

}
