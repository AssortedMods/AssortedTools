package com.grim3212.assorted.multitools.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.multitools.Constants;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class MultitoolsLanguageProvider extends LibLanguageProvider {

    public MultitoolsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.nameItems("(.+)_multitool", m -> titleCase(m.group(1)) + " MultiTool");

        this.add("manual." + Constants.FAMILY_ID + ".chapter.multitools", "Multitools");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.multitools.multitools.title", "Multitools");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.multitools.multitools",
                "A multitool is a sword, pickaxe, axe, shovel and hoe in one slot, and mines all of them at its "
                        + "material's speed.");
    }
}
