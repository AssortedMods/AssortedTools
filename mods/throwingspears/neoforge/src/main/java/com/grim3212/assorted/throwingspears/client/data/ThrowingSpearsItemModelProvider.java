package com.grim3212.assorted.throwingspears.client.data;

import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.client.ThrowingSpearsClient;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Throwing spears as vanilla draws a trident: flat in the GUI, on the ground and in frames, a model in the hand.
 * The item's own model is the in-hand one, drawn by {@code SpearItemRenderer}, which swaps in {@code <spear>_gui}
 * where vanilla would show the icon, and turns to {@code <spear>_throwing} while the spear is drawn back.
 */
public class ThrowingSpearsItemModelProvider extends ItemModelProvider {

    public ThrowingSpearsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Assorted Throwing Spears item models";
    }

    @Override
    protected void registerModels() {
        for (Item spear : ThrowingSpearsItems.spears()) {
            spear(spear);
        }
    }

    private void spear(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/" + name);

        this.withExistingParent(name + "_gui", "item/generated").texture("layer0", texture);
        this.withExistingParent(name + "_throwing", "item/trident_throwing").texture("particle", texture);
        // Only head is set; every other perspective comes from minecraft:item/trident_in_hand.
        this.withExistingParent(name, "item/trident_in_hand").texture("particle", texture)
                .transforms().transform(ItemDisplayContext.HEAD).rotation(0.0F, 180.0F, 120.0F).translation(8.0F, 10.0F, -11.0F).scale(1.5F).end().end()
                .override().predicate(ThrowingSpearsClient.THROWING, 1.0F).model(this.getExistingFile(modLoc("item/" + name + "_throwing"))).end();
    }
}
