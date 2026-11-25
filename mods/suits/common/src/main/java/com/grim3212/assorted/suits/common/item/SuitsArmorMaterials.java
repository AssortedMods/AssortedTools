package com.grim3212.assorted.suits.common.item;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.SuitsCommonMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;

/** The suits' armour materials, a registry on 1.21.1, registered from their configs before any piece is made. */
public final class SuitsArmorMaterials {

    public static final RegistryProvider<ArmorMaterial> ARMOR_MATERIALS = RegistryProvider.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);

    static {
        for (ArmorMaterialConfig material : SuitsCommonMod.COMMON_CONFIG.materials()) {
            material.register(ARMOR_MATERIALS);
        }
    }

    private SuitsArmorMaterials() {
    }

    /** Runs before {@link SuitsItems}, whose pieces each hold their material. */
    public static void init() {
    }
}
