package com.grim3212.assorted.suits.compat.jei;

import com.grim3212.assorted.suits.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIAssortedSuits implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Building an enchanted display stack needs the enchantment registry, which is the joined world's.
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        registration.addRecipes(RecipeTypes.ANVIL, AnvilRecipes.chickenEnchantRecipes(registration.getVanillaRecipeFactory(), registration.getIngredientManager(), minecraft.level.registryAccess()));
    }
}
