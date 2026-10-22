package com.grim3212.assorted.gearsets.client.data;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapters of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class GearSetsManualProvider extends LibManualProvider {

    public GearSetsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Constants.FAMILY_ID);
    }

    @Override
    protected void addChapters() {
        ChapterBuilder materials = this.chapter("materials", 0);
        materials.text("materials");
        // Emerald follows steel, as steel needs a mod that adds it and the page shows only what is loaded.
        materials.recipesById("basic", set("pickaxe", "axe", "shovel", "hoe", "sword"))
                .every(50)
                .opensEveryItem(id -> endsWithAny(id, "_pickaxe", "_axe", "_shovel", "_hoe", "_sword"));
        materials.recipesById("spears", recipeId("steel_spear"), recipeId("ruby_spear"), recipeId("emerald_spear")).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_spear"));

        this.chapter("armor", 90).recipesById("armor", set("helmet", "chestplate", "leggings", "boots"))
                .every(50)
                .opensEveryItem(id -> endsWithAny(id, "_helmet", "_chestplate", "_leggings", "_boots"));
    }

    private Identifier[] set(String... pieces) {
        Identifier[] ids = new Identifier[pieces.length * 2];
        for (int i = 0; i < pieces.length; i++) {
            ids[i] = recipeId("steel_" + pieces[i]);
            ids[pieces.length + i] = recipeId("emerald_" + pieces[i]);
        }
        return ids;
    }

    private static boolean endsWithAny(Identifier id, String... suffixes) {
        for (String suffix : suffixes) {
            if (id.getPath().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
