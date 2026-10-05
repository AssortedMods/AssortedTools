package com.grim3212.assorted.staffs.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.staffs.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class StaffsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<NeptuneStaffItem> NEPTUNE_STAFF = register("neptune_staff", props -> new NeptuneStaffItem(props.durability(StaffItem.DURABILITY)));
    public static final IRegistryObject<PhoenixStaffItem> PHOENIX_STAFF = register("phoenix_staff", props -> new PhoenixStaffItem(props.durability(StaffItem.DURABILITY).fireResistant()));
    // The cold counterparts of the blaze rod, blaze powder and fire charge, for the Neptune staff.
    public static final IRegistryObject<Item> FROST_ROD = register("frost_rod", Item::new);
    public static final IRegistryObject<Item> FROST_POWDER = register("frost_powder", Item::new);
    public static final IRegistryObject<IceChargeItem> ICE_CHARGE = register("ice_charge", IceChargeItem::new);
    public static final IRegistryObject<PowerStaffItem> POWER_STAFF = register("power_staff", props -> new PowerStaffItem(props.stacksTo(1)));

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
