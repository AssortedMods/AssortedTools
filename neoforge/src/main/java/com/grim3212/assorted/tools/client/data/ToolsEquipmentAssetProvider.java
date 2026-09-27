package com.grim3212.assorted.tools.client.data;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.tools.Constants;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Writes an {@code equipment_asset} for every armour material. Without one, armour renders
 * untextured on the player and nothing warns. Vanilla's {@code EquipmentAssetProvider} hardcodes
 * its own materials, so it cannot be extended.
 */
public class ToolsEquipmentAssetProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public ToolsEquipmentAssetProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> assets = new HashMap<>();

        List<ArmorMaterialConfig> materials = new ArrayList<>(List.of(ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial, ToolsCommonMod.COMMON_CONFIG.scubaSuitArmorMaterial, ToolsCommonMod.COMMON_CONFIG.lavaSuitArmorMaterial));
        materials.addAll(ToolsCommonMod.COMMON_CONFIG.moddedArmors.values());
        for (ArmorMaterialConfig material : materials) {
            assets.put(material.getAssetId(), EquipmentClientInfo.builder()
                    .addHumanoidLayers(Identifier.fromNamespaceAndPath(Constants.MOD_ID, material.getAssetId().identifier().getPath()))
                    .build());
        }

        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, assets);
    }

    @Override
    public String getName() {
        return "Equipment Asset Definitions: " + Constants.MOD_ID;
    }
}
