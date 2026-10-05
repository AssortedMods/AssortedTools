package com.grim3212.assorted.suits.client.data;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.suits.Constants;
import com.grim3212.assorted.suits.SuitsCommonMod;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** The equipment asset each suit's armor material names, drawing the humanoid layers of the same name. */
public class SuitsEquipmentAssetProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public SuitsEquipmentAssetProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> assets = new HashMap<>();

        for (ArmorMaterialConfig material : SuitsCommonMod.COMMON_CONFIG.materials()) {
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
