package com.grim3212.assorted.machetes.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.machetes.Constants;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class MachetesLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public MachetesLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("tag.item." + Constants.MOD_ID + ".machetes", "Machetes");

        this.nameItems("wood_(.+)", m -> "Wooden " + titleCase(m.group(1)));
        this.nameItems("gold_(.+)", m -> "Golden " + titleCase(m.group(1)));

        this.add("manual." + Constants.FAMILY_ID + ".chapter.machetes", "Machetes");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.machetes.machetes.title", "Machetes");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.machetes.machetes",
                "A machete is a lighter, quicker sword that cuts through leaves, vines, wool, cactus and the "
                        + "rest of the undergrowth." + BREAK
                        + "They come in every material, from wood up to netherite.");
    }
}
