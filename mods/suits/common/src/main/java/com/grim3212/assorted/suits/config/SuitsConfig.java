package com.grim3212.assorted.suits.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.suits.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.List;
import java.util.function.Supplier;

public class SuitsConfig {

    public final ArmorMaterialConfig chickenSuitArmorMaterial;
    public final ArmorMaterialConfig scubaSuitArmorMaterial;
    public final ArmorMaterialConfig lavaSuitArmorMaterial;

    public SuitsConfig() {
        // Needed at registration: the armor bakes its material in as it is constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        this.chickenSuitArmorMaterial = armor(builder, "chicken_suit", 5, 15, new int[]{1, 2, 3, 1}, () -> BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE), LibCommonTags.Items.FEATHERS, "chicken_suit");
        this.scubaSuitArmorMaterial = armor(builder, "scuba_suit", 12, 12, new int[]{1, 3, 4, 2}, () -> SoundEvents.ARMOR_EQUIP_LEATHER, LibCommonTags.Items.LEATHER, "scuba");
        this.lavaSuitArmorMaterial = armor(builder, "lava_suit", 20, 9, new int[]{2, 6, 7, 2}, () -> SoundEvents.ARMOR_EQUIP_NETHERITE, LibCommonTags.Items.INGOTS_NETHERITE, "lava");

        builder.setup();
    }

    public List<ArmorMaterialConfig> materials() {
        return List.of(this.chickenSuitArmorMaterial, this.scubaSuitArmorMaterial, this.lavaSuitArmorMaterial);
    }

    private static ArmorMaterialConfig armor(IConfigurationBuilder builder, String name, int durability, int enchantability, int[] reductionAmounts, Supplier<Holder<SoundEvent>> equipSound, TagKey<Item> repairItems, String asset) {
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, asset));
        return new ArmorMaterialConfig(builder, name, name, durability, enchantability, 0.0F, 0.0F, reductionAmounts, equipSound, repairItems, assetId);
    }
}
