package com.grim3212.assorted.buckets.gametest;

import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Optional;

/**
 * The bucket tooltip as Fabric builds it, which only happens on the client, and the fluid textures a bucket draws.
 * Run with {@code ./gradlew :buckets:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class BucketsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // Inside a world: an item's default components are only bound once a world's registries have loaded.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                List<String> bucket = tooltipKeys(client, BucketsItems.WOOD.bucket().get().getEmptyStack());
                if (!bucket.contains("tooltip.buckets.empty")) {
                    throw new AssertionError("an empty bucket's tooltip is " + bucket);
                }

                assertFluidTextures(Fluids.WATER);
                assertFluidTextures(Fluids.LAVA);
            });
        }
    }

    /**
     * A fluid's still and flowing textures, asked through the client fluid helper and through the fluid variant
     * handler a foreign fluid gets. Both must agree and neither may be the missing texture.
     */
    private static void assertFluidTextures(Fluid fluid) {
        FluidInformation contents = new FluidInformation(fluid);

        Identifier still = ClientServices.FLUIDS.getStillFluidTexture(fluid);
        Identifier flowing = ClientServices.FLUIDS.getFlowingFluidTexture(fluid);
        if (still.equals(MissingTextureAtlasSprite.getLocation()) || flowing.equals(MissingTextureAtlasSprite.getLocation())) {
            throw new AssertionError(fluid + " resolved to the missing texture: still " + still + ", flowing " + flowing);
        }

        IFluidVariantHandler handler = Services.FLUIDS.getVariantHandlerFor(contents)
                .orElseThrow(() -> new AssertionError("no fluid variant handler for " + fluid));

        Optional<Identifier> handlerStill = handler.getStillTexture(contents);
        Optional<Identifier> handlerFlowing = handler.getFlowingTexture(contents);
        if (!handlerStill.equals(Optional.of(still)) || !handlerFlowing.equals(Optional.of(flowing))) {
            throw new AssertionError("the variant handler for " + fluid + " answered still " + handlerStill
                    + " and flowing " + handlerFlowing + ", but the client helper says " + still + " and " + flowing);
        }
    }

    private static List<String> tooltipKeys(Minecraft client, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                .toList();
    }
}
