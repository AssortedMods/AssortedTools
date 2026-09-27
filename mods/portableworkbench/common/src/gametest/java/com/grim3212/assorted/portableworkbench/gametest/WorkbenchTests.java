package com.grim3212.assorted.portableworkbench.gametest;

import com.grim3212.assorted.lib.test.TestSupport;
import com.grim3212.assorted.portableworkbench.common.inventory.PortableWorkbenchMenu;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** The workbench opens a crafting grid from the hand, and JEI finds it on Fabric. */
final class WorkbenchTests {

    private WorkbenchTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("portable_workbench_opens_a_crafting_grid", WorkbenchTests::opensACraftingGrid);
        out.accept("jei_plugin_is_registered_on_fabric", WorkbenchTests::jeiPluginIsRegisteredOnFabric);
    }

    /** The menu lasts while the workbench is in either hand, and closes once it is put away. */
    private static void opensACraftingGrid(GameTestHelper helper) {
        ServerPlayer player = TestSupport.survivalPlayer(helper, new ItemStack(PortableWorkbenchItems.PORTABLE_WORKBENCH.get()));
        PortableWorkbenchItems.PORTABLE_WORKBENCH.get().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.assertTrue(player.containerMenu instanceof PortableWorkbenchMenu, "using the workbench opened " + player.containerMenu + ", not its crafting menu");
        helper.assertTrue(player.containerMenu.stillValid(player), "the menu closed while the workbench was still held");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        helper.assertFalse(player.containerMenu.stillValid(player), "the menu stayed open after the workbench was put away");
        helper.succeed();
    }

    private static void jeiPluginIsRegisteredOnFabric(GameTestHelper helper) {
        TestSupport.assertJeiPluginIsRegistered(helper, "assortedportableworkbench", "com.grim3212.assorted.portableworkbench.compat.jei.JEIAssortedPortableWorkbench");
        helper.succeed();
    }
}
