package com.grim3212.assorted.throwingspears.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.throwingspears.Constants;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class ThrowingSpearsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public ThrowingSpearsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        String enchantment = "enchantment." + Constants.MOD_ID + ".";
        this.add(enchantment + "conductive", "Conductive");
        this.add(enchantment + "conductive.desc", "When a throwing spear hits something there will be a chance for it to cause a lightning strike at that location.");
        this.add(enchantment + "flammable", "Flammable");
        this.add(enchantment + "flammable.desc", "When a throwing spear hits something there will be a chance for it to cause fire to be created at that location.");
        this.add(enchantment + "unstable", "Unstable");
        this.add(enchantment + "unstable.desc", "When a throwing spear hits something there will be an explosion at that location.");
        this.add(enchantment + "bounciness", "Bounciness");
        this.add(enchantment + "bounciness.desc", "When a throwing spear hit a block it will be able to bounce a number of times dependant on the level of bounciness.");

        this.add("death.attack." + Constants.MOD_ID + ".spear", "%1$s was speared by %2$s");
        this.add("death.attack." + Constants.MOD_ID + ".spear.item", "%1$s was speared by %2$s with %3$s");
        this.add("entity." + Constants.MOD_ID + ".better_spear", "Throwing Spear");
        this.add("tag.item." + Constants.MOD_ID + ".enchantable.throwing_spear", "Enchantable Throwing Spears");

        this.nameItems("(.+)_throwing_spear", m -> material(m.group(1)) + " Throwing Spear");

        this.add("manual." + Constants.FAMILY_ID + ".chapter.throwing_spears", "Throwing Spears");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.throwing_spears.throwing_spears.title", "Throwing Spears");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.throwing_spears.throwing_spears",
                "A throwing spear is thrown with right click and sticks where it lands, ready to be picked "
                        + "back up." + BREAK
                        + "They come in every material and also make sure you checkout the enchantment table for some fun with these.");
    }

    /** The vanilla tiers read as the adjective, the rest as their id. */
    private static String material(String id) {
        return switch (id) {
            case "wood" -> "Wooden";
            case "gold" -> "Golden";
            default -> titleCase(id);
        };
    }
}
