package com.grim3212.assorted.wands.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.Family;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the wands'
 * messages, which keep the keys they had in Assorted Tools, the lines every part shares, and this part's manual chapter.
 */
public class WandsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public WandsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("assortedtools.wand.mode.buildbox", "\u00A77Build box");
        this.add("assortedtools.wand.mode.buildroom", "\u00A77Build room");
        this.add("assortedtools.wand.mode.buildframe", "\u00A77Build frame");
        this.add("assortedtools.wand.mode.buildtorches", "\u00A7eBuild Torches");
        this.add("assortedtools.wand.mode.buildwater", "\u00A79Fill water");
        this.add("assortedtools.wand.mode.buildlava", "\u00A7cFill lava");
        this.add("assortedtools.wand.mode.buildcaves", "\u00A78Fill caves");
        this.add("assortedtools.wand.mode.breakweak", "\u00A7aBreak Weak");
        this.add("assortedtools.wand.mode.breakall", "\u00A74Break All");
        this.add("assortedtools.wand.mode.breakxores", "\u00A7bLeave Ores");
        this.add("assortedtools.wand.mode.mineall", "\u00A74Mine all");
        this.add("assortedtools.wand.mode.minedirt", "\u00A78Mine dirt");
        this.add("assortedtools.wand.mode.minewood", "\u00A7aMine wood");
        this.add("assortedtools.wand.mode.mineores", "\u00A7bSurface mine");
        this.add("assortedtools.wand.current", "\u00A7lCurrent\u00A7r: %s");
        this.add("assortedtools.wand.broken", "\u00A7lThis wand got screwed up.");
        this.add("assortedtools.wand.switched", "Switched wand mode to %s");
        this.add("result.wand.fill", "%s blocks filled up with stone.");
        this.add("result.wand.mine", "No ores found.");
        this.add("error.wand.nocave", "No caves were found.");
        this.add("error.wand.nowork", "No work to do.");
        this.add("error.wand.nostart", "You didn't select the starting block!");
        this.add("error.wand.cantbuild", "Can't build this block!");
        this.add("error.wand.toofar", "That's too far!");
        this.add("error.wand.toofewitems", "You don't have enough items (needed %s, you have %s).");
        this.add("error.wand.toomany", "Too many blocks to dig (%s, limit=%s).");
        this.add("error.wand.notsamecorner", "You can't do this! Corner blocks must be the same.");
        this.add("error.wand.cantfillcave", "You need a REINFORCED wand for cave filling!");
        this.add("error.wand.cantfilllava", "You need a REINFORCED wand for lava filling!");
        this.add("error.wand.cantfillwater", "You need a REINFORCED wand for water filling!");
        this.add("error.wand.cantminesurface", "You need a REINFORCED wand for surface mining!");
        this.add("error.wand.toofewlava", "You don't have enough lava buckets.");
        this.add("error.wand.toofewwater", "You need two buckets of water.");

        this.add("manual." + Family.ID + ".chapter.wands", "Wands");
        this.add("manual." + Family.ID + ".chapter.wands.modes.title", "How Wands Work");
        this.add("manual." + Family.ID + ".chapter.wands.modes",
                "Each wand has several modes and you switch between them with the mode keybind, Z by default." + BREAK
                        + "A wand works on a region rather than a block, so it will tell you when you have picked too much, when the corners do not match, "
                        + "or when you do not have the blocks to finish the job. Nothing happens until it can be done properly." + BREAK
                        + "Wands have a limited number of uses.");
        this.add("manual." + Family.ID + ".chapter.wands.basic.title", "Basic Wands");
        this.add("manual." + Family.ID + ".chapter.wands.basic",
                "Three wands. The building wand puts up boxes, frames, rooms and lines of torches. The mining wand clears wood, dirt or ore. "
                        + "The breaking wand takes everything out, or leaves the ores where they are if you ask it to.");
        this.add("manual." + Family.ID + ".chapter.wands.reinforced.title", "Reinforced Wands");
        this.add("manual." + Family.ID + ".chapter.wands.reinforced",
                "The reinforced version of each wand lasts far longer and unlocks the modes the basic one refuses." + BREAK
                        + "Filling a cave, flooding a space with water, or pouring lava into one, and surface mining, all need a reinforced wand.");
    }
}
