package com.grim3212.assorted.portableworkbench.common.inventory;

import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

/**
 * The crafting table's menu, valid while the player holds a portable workbench rather than while
 * they stand next to a table. Keeps {@code MenuType.CRAFTING} so the client opens vanilla's own screen.
 */
public class PortableWorkbenchMenu extends CraftingMenu {

    /**
     * {@code access} has to be a real position: {@code CraftingMenu#removed} hands the grid back to
     * the player through it, and {@code ContainerLevelAccess.NULL} would delete what was left there.
     */
    public PortableWorkbenchMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().getItem() instanceof PortableWorkbenchItem || player.getOffhandItem().getItem() instanceof PortableWorkbenchItem;
    }
}
