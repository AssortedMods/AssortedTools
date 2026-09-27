package com.grim3212.assorted.extramaterials.client.data;

import com.grim3212.assorted.extramaterials.Constants;
import com.grim3212.assorted.extramaterials.common.item.ExtraMaterialsItems;
import com.grim3212.assorted.extramaterials.common.item.MaterialSet;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ExtraMaterialsItemModelProvider extends ModelProvider {

    public ExtraMaterialsItemModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public String getName() {
        return "Assorted Extra Materials item models";
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (MaterialSet set : ExtraMaterialsItems.MATERIALS.values()) {
            for (Item tool : new Item[]{set.sword().get(), set.pickaxe().get(), set.axe().get(), set.shovel().get(), set.hoe().get()}) {
                itemModels.generateFlatItem(tool, ModelTemplates.FLAT_HANDHELD_ITEM);
            }
            lungeSpear(itemModels, set.spear().get());
            for (Item armor : new Item[]{set.helmet().get(), set.chestplate().get(), set.leggings().get(), set.boots().get()}) {
                itemModels.generateFlatItem(armor, ModelTemplates.FLAT_ITEM);
            }
        }
    }

    /** As vanilla draws its spears: flat in the GUI, a longer in-hand sprite otherwise. */
    private void lungeSpear(ItemModelGenerators itemModels, Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        Identifier flat = ModelTemplates.FLAT_ITEM.create(modelId(name), TextureMapping.layer0(texture(name)), itemModels.modelOutput);
        Identifier inHand = ModelTemplates.SPEAR_IN_HAND.create(modelId(name + "_in_hand"), TextureMapping.layer0(texture(name + "_in_hand")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(ItemModelUtils.plainModel(flat), ItemModelUtils.plainModel(inHand)), new ClientItem.Properties(true, false, 1.95F));
    }

    private static Identifier modelId(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path);
    }

    private static Material texture(String path) {
        return new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + path));
    }
}
