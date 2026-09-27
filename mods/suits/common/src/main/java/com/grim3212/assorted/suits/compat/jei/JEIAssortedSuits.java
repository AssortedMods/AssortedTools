package com.grim3212.assorted.suits.compat.jei;

import com.grim3212.assorted.suits.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

@JeiPlugin
public class JEIAssortedSuits implements IModPlugin {

    private static final Identifier PLUGIN_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "jei");

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Building an enchanted display stack needs a registry lookup, which JEI hands over under the vanilla slot-display key.
        HolderLookup.Provider registries = registration.getContextMap().getOptional(SlotDisplayContext.REGISTRIES);
        if (registries == null) {
            return;
        }

        registration.addRecipes(RecipeTypes.ANVIL, AnvilRecipes.chickenEnchantRecipes(registration.getVanillaRecipeFactory(), registration.getIngredientManager(), registries));
    }
}
