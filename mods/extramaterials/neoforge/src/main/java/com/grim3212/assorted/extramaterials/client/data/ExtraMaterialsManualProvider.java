package com.grim3212.assorted.extramaterials.client.data;

import com.grim3212.assorted.extramaterials.Constants;
import com.grim3212.assorted.extramaterials.Family;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapters of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class ExtraMaterialsManualProvider extends LibManualProvider {

    public ExtraMaterialsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder materials = this.chapter("materials", 0);
        materials.text("materials");
        materials.recipesById("basic", recipeId("steel_pickaxe"), recipeId("steel_axe"), recipeId("steel_shovel"), recipeId("steel_hoe"), recipeId("steel_sword"))
                .every(50)
                .opensEveryItem(id -> endsWithAny(id, "_pickaxe", "_axe", "_shovel", "_hoe", "_sword"));
        materials.recipesById("spears", recipeId("steel_spear"), recipeId("ruby_spear"), recipeId("emerald_spear")).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_spear"));

        this.chapter("armor", 90).recipesById("armor", recipeId("steel_helmet"), recipeId("steel_chestplate"), recipeId("steel_leggings"), recipeId("steel_boots"))
                .every(50)
                .opensEveryItem(id -> endsWithAny(id, "_helmet", "_chestplate", "_leggings", "_boots"));
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
