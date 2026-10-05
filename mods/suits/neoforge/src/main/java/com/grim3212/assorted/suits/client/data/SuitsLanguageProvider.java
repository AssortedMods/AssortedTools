package com.grim3212.assorted.suits.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.suits.Constants;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. The suit pieces read differently from their ids, so each is named here, beside the
 * lines every Assorted Tools part shares and this part's manual chapter.
 */
public class SuitsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public SuitsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.item("chicken_suit_helmet", "Chicken Hat");
        this.item("chicken_suit_chestplate", "Chicken Suit");
        this.item("chicken_suit_leggings", "Chicken Legs");
        this.item("chicken_suit_boots", "Chicken Boots");
        this.item("scuba_helmet", "Scuba Mask");
        this.item("scuba_chestplate", "Scuba Tank");
        this.item("scuba_leggings", "Scuba Legs");
        this.item("scuba_boots", "Scuba Fins");
        this.item("lava_helmet", "Lava Hood");
        this.item("lava_chestplate", "Lava Suit");
        this.item("lava_leggings", "Lava Legs");
        this.item("lava_boots", "Lava Boots");

        this.add("enchantment." + Constants.MOD_ID + ".chicken_jump", "Chicken Jump");
        this.add("enchantment." + Constants.MOD_ID + ".chicken_jump.desc", "Lets you hover like a chicken as well as get one extra jump per armor enchanted with Chicken Jump");
        this.add("effect." + Constants.MOD_ID + ".lava_striding", "Lava Striding");

        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits", "Suits");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.chicken_suit.title", "Chicken Suit");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.chicken_suit",
                "The chicken suit is not about protection. Every piece you are wearing gives you one more jump "
                        + "in the air, so the full set is four jumps." + BREAK
                        + "It is not flying. It is close enough to get across most things, and to get you down "
                        + "off most of the rest safely." + BREAK
                        + "Try combining them with the corresponding piece in an Anvil to see what happens.");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.scuba_suit.title", "Scuba Suit");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.scuba_suit",
                "The scuba suit works in halves to allow for an easier time under water." + BREAK
                        + "The Mask and tank together let you breathe under water and see better than you would without it." + BREAK
                        + "The Legs and fins together move you through water far quicker than swimming.");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.lava_suit.title", "Lava Suit");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.suits.lava_suit",
                "The lava suit is like the scuba suit but for lava." + BREAK
                        + "You will need all four pieces to avoid burning alive though." + BREAK
                        + "The Legs and boots on their own let you swim through lava as you would through water.");
    }

    private void item(String path, String name) {
        this.add("item." + Constants.MOD_ID + "." + path, name);
    }
}
