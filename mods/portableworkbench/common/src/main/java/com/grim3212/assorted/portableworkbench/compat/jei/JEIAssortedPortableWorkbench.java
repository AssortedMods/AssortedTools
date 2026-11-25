package com.grim3212.assorted.portableworkbench.compat.jei;

import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.common.item.PortableWorkbenchItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIAssortedPortableWorkbench implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    /** It opens vanilla's crafting menu, so JEI's own crafting transfer already fills it; it only needs listing beside the crafting table. */
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PortableWorkbenchItems.PORTABLE_WORKBENCH.get(), RecipeTypes.CRAFTING);
    }
}
