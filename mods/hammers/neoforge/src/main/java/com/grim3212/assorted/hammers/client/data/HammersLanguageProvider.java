package com.grim3212.assorted.hammers.client.data;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class HammersLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public HammersLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.nameItems("wood_(.+)", m -> "Wooden " + titleCase(m.group(1)));
        this.nameItems("gold_(.+)", m -> "Golden " + titleCase(m.group(1)));

        this.add("manual." + Constants.FAMILY_ID + ".chapter.hammers", "Hammers");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.hammers.hammers.title", "Hammers");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.hammers.hammers",
                "A hammer destroys a block in one hit and drops nothing at all." + BREAK
                        + "Be careful where you use it but they can come in handy.");
    }
}
