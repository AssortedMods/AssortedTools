package com.grim3212.assorted.pokeball.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.pokeball.Constants;
import com.grim3212.assorted.pokeball.Family;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the lines
 * every Assorted Tools part shares, the tooltips and this part's manual chapter.
 */
public class PokeballLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public PokeballLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("tooltip.pokeball.stored", "Stored: %s");
        this.add("tooltip.pokeball.stored_custom_name", "Stored: %s (%s)");
        this.add("tooltip.pokeball.empty", "Empty");
        this.add("tag.item.assorteddecor.cage_supported", "Cage Supported Items");

        this.add("manual." + Family.ID + ".chapter.pokeball", "Pokeball");
        this.add("manual." + Family.ID + ".chapter.pokeball.pokeball.title", "Pokeball");
        this.add("manual." + Family.ID + ".chapter.pokeball.pokeball",
                "Throw a pokeball at a mob and it goes inside, exactly as it was. So health, name, anything it "
                        + "was carrying. Throw it again and the mob comes back out." + BREAK
                        + "A hostile mob let out of a pokeball is still hostile, and still remembers you so be careful.");
    }
}
