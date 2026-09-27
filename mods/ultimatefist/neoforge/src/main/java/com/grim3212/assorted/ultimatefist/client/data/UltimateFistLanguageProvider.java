package com.grim3212.assorted.ultimatefist.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.ultimatefist.Constants;
import com.grim3212.assorted.ultimatefist.Family;
import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class UltimateFistLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public UltimateFistLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        // Each letter in its own colour, so the eight together spell the fist's name.
        this.add("item." + Constants.MOD_ID + ".u_fragment", "§1U§r Fragment");
        this.add("item." + Constants.MOD_ID + ".l_fragment", "§2L§r Fragment");
        this.add("item." + Constants.MOD_ID + ".t_fragment", "§4T§r Fragment");
        this.add("item." + Constants.MOD_ID + ".i_fragment", "§5I§r Fragment");
        this.add("item." + Constants.MOD_ID + ".m_fragment", "§6M§r Fragment");
        this.add("item." + Constants.MOD_ID + ".a_fragment", "§8A§r Fragment");
        this.add("item." + Constants.MOD_ID + ".missing_fragment", "§k???§r Fragment");
        this.add("item." + Constants.MOD_ID + ".e_fragment", "§dE§r Fragment");
        this.add(FragmentItem.DESCRIPTION_KEY, "A fragment of a powerful tool from an ancient civilization");
        this.add("tag.item." + Constants.MOD_ID + ".ultimate_fragments", "Ultimate Fragments");

        this.add("manual." + Family.ID + ".chapter.ultimate", "The Ultimate Fist");
        this.add("manual." + Family.ID + ".chapter.ultimate.fragments.title", "Fragments");
        this.add("manual." + Family.ID + ".chapter.ultimate.fragments",
                "Eight fragments of something an older civilization built, found in chests across Minecraft." + BREAK
                        + "Laid out in order they spell out what they came from.");
        this.add("manual." + Family.ID + ".chapter.ultimate.fist.title", "The Ultimate Fist");
        this.add("manual." + Family.ID + ".chapter.ultimate.fist",
                "All eight fragments and a nether star make the ultimate fist. It is extremely powerful and out "
                        + "of the box it mines very fast and hits hard enough to kill most things in one hit.");
    }
}
