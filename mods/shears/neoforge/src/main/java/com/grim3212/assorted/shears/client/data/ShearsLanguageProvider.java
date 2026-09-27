package com.grim3212.assorted.shears.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.Family;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class ShearsLanguageProvider extends LibLanguageProvider {

    public ShearsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.nameItems("wood_(.+)", m -> "Wooden " + titleCase(m.group(1)));
        this.nameItems("gold_(.+)", m -> "Golden " + titleCase(m.group(1)));

        this.add("enchantment." + Constants.MOD_ID + ".coral_cutter", "Coral Cutter");
        this.add("enchantment." + Constants.MOD_ID + ".coral_cutter.desc", "Lets you harvest Coral with this enchantment instead of needing a tool with Silk Touch.");
        this.add("tag.item." + Constants.MOD_ID + ".enchantable.shears", "Enchantable Shears");

        this.add("manual." + Family.ID + ".chapter.shears", "Shears");
        this.add("manual." + Family.ID + ".chapter.shears.shears.title", "Shears");
        this.add("manual." + Family.ID + ".chapter.shears.shears",
                "Shears in every material, from wood up to netherite. Also lookout for a new Shears enchantment that lets you harvest coral without needing Silk Touch.");
    }
}
