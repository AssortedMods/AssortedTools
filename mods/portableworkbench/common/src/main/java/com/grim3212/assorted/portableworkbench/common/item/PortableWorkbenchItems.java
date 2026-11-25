package com.grim3212.assorted.portableworkbench.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.portableworkbench.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class PortableWorkbenchItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<PortableWorkbenchItem> PORTABLE_WORKBENCH = ITEMS.register("portable_workbench",
            () -> new PortableWorkbenchItem(new Item.Properties().stacksTo(1)));

    public static void init() {
    }
}
