package com.grim3212.assorted.boomerangs.client.data;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.Family;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class BoomerangsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public BoomerangsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("death.attack." + Constants.MOD_ID + ".boomerang", "%1$s was boomerang'd by %2$s");
        this.add("death.attack." + Constants.MOD_ID + ".boomerang.item", "%1$s was boomerang'd by %2$s with %3$s");

        this.nameItems("wood_(.+)", m -> "Wooden " + titleCase(m.group(1)));

        this.add("manual." + Family.ID + ".chapter.boomerangs", "Boomerangs");
        this.add("manual." + Family.ID + ".chapter.boomerangs.boomerangs.title", "Boomerangs");
        this.add("manual." + Family.ID + ".chapter.boomerangs.boomerangs",
                "A boomerang is thrown with right click, flies out to its limit and comes back to you. It hurts "
                        + "what it passes through on the way." + BREAK
                        + "The diamond one goes further and hits harder, and can be set to follow where you are "
                        + "looking rather than flying straight." + BREAK
                        + "They might even pickup some items on the way.");
    }
}
