package com.grim3212.assorted.ultimatefist.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.ultimatefist.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.function.Function;

public class UltimateFistItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<FragmentItem> U_FRAGMENT = fragment("u_fragment");
    public static final IRegistryObject<FragmentItem> L_FRAGMENT = fragment("l_fragment");
    public static final IRegistryObject<FragmentItem> T_FRAGMENT = fragment("t_fragment");
    public static final IRegistryObject<FragmentItem> I_FRAGMENT = fragment("i_fragment");
    public static final IRegistryObject<FragmentItem> M_FRAGMENT = fragment("m_fragment");
    public static final IRegistryObject<FragmentItem> A_FRAGMENT = fragment("a_fragment");
    public static final IRegistryObject<FragmentItem> MISSING_FRAGMENT = fragment("missing_fragment");
    public static final IRegistryObject<FragmentItem> E_FRAGMENT = fragment("e_fragment");

    public static final IRegistryObject<UltimateFistItem> ULTIMATE_FIST = register("ultimate_fist", props -> new UltimateFistItem(props.rarity(Rarity.EPIC)));

    /** In the order they spell out. */
    public static List<FragmentItem> fragments() {
        return List.of(U_FRAGMENT.get(), L_FRAGMENT.get(), T_FRAGMENT.get(), I_FRAGMENT.get(), M_FRAGMENT.get(), A_FRAGMENT.get(), MISSING_FRAGMENT.get(), E_FRAGMENT.get());
    }

    private static IRegistryObject<FragmentItem> fragment(String name) {
        return register(name, props -> new FragmentItem(props.rarity(Rarity.RARE)));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
