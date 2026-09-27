package com.grim3212.assorted.extramaterials.client.data;

import com.grim3212.assorted.extramaterials.Constants;
import com.grim3212.assorted.extramaterials.Family;
import com.grim3212.assorted.extramaterials.api.ExtraMaterialsTags;
import com.grim3212.assorted.extramaterials.data.ExtraMaterialsItemTagProvider;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. Every item's name is its id in title case; these are the lines every Assorted Tools
 * part shares, the convention tags' names, and this part's manual chapters.
 */
public class ExtraMaterialsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public ExtraMaterialsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        for (String material : ExtraMaterialsItemTagProvider.materialToolNames()) {
            for (String kind : ExtraMaterialsTags.MATERIAL_TOOL_KINDS) {
                this.add("tag.item.c." + kind + "." + material, titleCase(material) + " " + titleCase(kind));
            }
        }

        String chapter = "manual." + Family.ID + ".chapter.";
        this.add(chapter + "materials", "Extra Materials");
        this.add(chapter + "materials.materials.title", "Materials");
        this.add(chapter + "materials.materials",
                "Every metal and gem Assorted Core adds gets a full tool set here, so tin, silver, bronze, "
                        + "steel, ruby and the rest of them." + BREAK
                        + "Vanilla's own materials are not repeated as pickaxes and swords, since the game "
                        + "already has those. They do turn up in the tools vanilla has no version of though like hammers, "
                        + "multitools, shears, spears and buckets all go from wood up to netherite.");
        this.add(chapter + "materials.basic.title", "The Basic Set");
        this.add(chapter + "materials.basic",
                "Pickaxe, axe, shovel, hoe and sword, in every material. They behave exactly as the vanilla ones do.");
        this.add(chapter + "materials.spears.title", "Spears");
        this.add(chapter + "materials.spears",
                "These spears act exactly like the Vanilla spears in Minecraft but in all of the different materials we support.");

        this.add(chapter + "armor", "Armor");
        this.add(chapter + "armor.armor.title", "Armor Sets");
        this.add(chapter + "armor.armor",
                "A full four piece set for every material this mod adds tools for, on the same ladder the "
                        + "tools use.");
    }
}
