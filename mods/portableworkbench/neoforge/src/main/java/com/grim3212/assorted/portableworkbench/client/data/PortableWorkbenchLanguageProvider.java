package com.grim3212.assorted.portableworkbench.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.portableworkbench.Constants;
import com.grim3212.assorted.portableworkbench.Family;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the lines
 * every Assorted Tools part shares, and this part's manual chapter.
 */
public class PortableWorkbenchLanguageProvider extends LibLanguageProvider {

    public PortableWorkbenchLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("manual." + Family.ID + ".chapter.portable_workbench", "Portable Workbench");
        this.add("manual." + Family.ID + ".chapter.portable_workbench.portable_workbench.title", "Portable Workbench");
        this.add("manual." + Family.ID + ".chapter.portable_workbench.portable_workbench", "A crafting table you can carry. Right click with it to craft on the spot.");
    }
}
