package com.grim3212.assorted.extramaterials.config;

import com.grim3212.assorted.extramaterials.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.HashMap;
import java.util.Map;

/** Each extra material's armor and spear numbers; its tool numbers are the shared tier's in Lib. */
public class ExtraMaterialsConfig {

    // Keyed by tier name.
    public final Map<String, SpearConfig> spears = new HashMap<>();
    public final Map<String, ArmorMaterialConfig> armors = new HashMap<>();

    public ExtraMaterialsConfig() {
        // Needed at registration: items bake their armor and spear values in as they are constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        extra(builder, "tin", 8, 14, 0.0F, 0.0F, new int[]{1, 3, 5, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "copper", 11, 14, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "silver", 27, 14, 0.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "aluminum", 13, 10, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "nickel", 13, 10, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "platinum", 36, 18, 3.0F, 0.2F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "lead", 13, 4, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "bronze", 14, 13, 0.0F, 0.1F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "electrum", 13, 13, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "invar", 15, 11, 0.2F, 0.1F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "steel", 26, 10, 0.5F, 0.3F, new int[]{2, 6, 7, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "ruby", 34, 10, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "amethyst", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "sapphire", 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "topaz", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "emerald", 32, 14, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "peridot", 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);

        builder.setup();
    }

    private void extra(IConfigurationBuilder builder, String name, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Holder<SoundEvent> equipSound) {
        ToolTier tier = ToolTiers.get().extra(name);
        this.spears.put(name, new SpearConfig(builder, "spears", name, tier.getHarvestLevel()));
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        this.armors.put(name, new ArmorMaterialConfig(builder, "extra_armor", name, durability, enchantability, toughness, knockbackResistance, reductionAmounts, () -> equipSound, tier.getRepairItems(), assetId));
    }
}
