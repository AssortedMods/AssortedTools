package com.grim3212.assorted.throwingspears.client;

import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.client.render.entity.BetterSpearRenderer;
import com.grim3212.assorted.throwingspears.client.render.item.SpearItemRenderer;
import com.grim3212.assorted.throwingspears.client.render.model.SpearModel;
import com.grim3212.assorted.throwingspears.client.render.model.ThrowingSpearsModelLayers;
import com.grim3212.assorted.throwingspears.common.entity.ThrowingSpearsEntities;
import com.grim3212.assorted.throwingspears.common.item.BetterSpearItem;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class ThrowingSpearsClient {

    /** What the item models' {@code overrides} read to swap to the throwing pose, as vanilla's trident reads {@code throwing}. */
    public static final ResourceLocation THROWING = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "throwing");

    /**
     * @param standaloneModels the loader's lookup for a model it was asked to bake on its own, one of
     *                         {@link #iconModels()}
     */
    public static void init(Function<ResourceLocation, BakedModel> standaloneModels) {
        ClientServices.CLIENT.registerEntityLayer(ThrowingSpearsModelLayers.SPEAR, SpearModel::createLayer);
        ClientServices.CLIENT.registerEntityRenderer(() -> ThrowingSpearsEntities.BETTER_SPEAR.get(), BetterSpearRenderer::new);

        ClientServices.CLIENT.registerSpecialModelRenderers(register -> ThrowingSpearsItems.ITEMS.getEntries().forEach(entry -> {
            if (entry.get() instanceof BetterSpearItem spear) {
                register.registerItemRenderer(() -> spear, () -> new SpearItemRenderer(SpearModel.texture(spear), iconModel(spear), standaloneModels));
            }
        }));
        ThrowingSpearsItems.ITEMS.getEntries().forEach(entry -> ClientServices.CLIENT.registerItemProperty(entry::get, THROWING,
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }

    /** The flat icon of every spear, which the loader has to bake as a standalone model for {@link SpearItemRenderer}. */
    public static List<ResourceLocation> iconModels() {
        return ThrowingSpearsItems.spears().stream().map(ThrowingSpearsClient::iconModel).toList();
    }

    private static ResourceLocation iconModel(BetterSpearItem spear) {
        return BuiltInRegistries.ITEM.getKey(spear).withPath(path -> "item/" + path + "_gui");
    }
}
