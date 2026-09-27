package com.grim3212.assorted.staffs.client.data;

import com.grim3212.assorted.lib.data.LibLanguageProvider;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.Family;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the staffs'
 * modes, which keep the keys they had in Assorted Tools, the lines every part shares, and this part's manual chapter.
 */
public class StaffsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public StaffsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Family.ID, "Assorted Tools");
        this.add("manual." + Family.ID + ".title", "Assorted Tools");
        this.add("manual." + Family.ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("tag.item.c.rods.frost", "Frost Rods");
        this.add("assortedtools.staff.mode.place_water", "Place Water");
        this.add("assortedtools.staff.mode.freeze_mobs", "Freeze Mobs");
        this.add("assortedtools.staff.mode.freeze_water", "Freeze Water");
        this.add("assortedtools.staff.mode.place_lava", "Place Lava");
        this.add("assortedtools.staff.mode.place_fire", "Place Fire");
        this.add("assortedtools.staff.mode.thaw_mobs", "Thaw Mobs");
        this.add("assortedtools.staff.mode.melt_ice", "Melt Ice");
        this.add("assortedtools.staff.mode.float_push", "Floating Push");
        this.add("assortedtools.staff.mode.float_pull", "Floating Pull");
        this.add("assortedtools.staff.mode.drop_push", "Dropping Push");
        this.add("assortedtools.staff.mode.drop_pull", "Dropping Pull");
        this.add("assortedtools.staff.current", "\u00A7lMode\u00A7r: %s");
        this.add("assortedtools.staff.switched", "Switched staff mode to %s");

        this.add("manual." + Family.ID + ".chapter.staffs", "Staffs");
        this.add("manual." + Family.ID + ".chapter.staffs.neptune.title", "Neptune Staff");
        this.add("manual." + Family.ID + ".chapter.staffs.neptune",
                "Switch its mode with the tool mode key, Z by default." + BREAK
                        + "Place Water pours a water source where you click. Freeze Mobs freezes every mob around you solid, and Freeze Water turns the still water around you to ice." + BREAK
                        + "It is no weapon, but anything it hits is frozen where it stands.");
        this.add("manual." + Family.ID + ".chapter.staffs.phoenix.title", "Phoenix Staff");
        this.add("manual." + Family.ID + ".chapter.staffs.phoenix",
                "The Neptune staff's the opposite. Place Lava and Place Fire where you click, Thaw Mobs frees every frozen mob around you, and Melt Ice melts the ice around you." + BREAK
                        + "A hit with it thaws a frozen mob.");
        this.add("manual." + Family.ID + ".chapter.staffs.frost.title", "Frost Rods");
        this.add("manual." + Family.ID + ".chapter.staffs.frost",
                "The blaze rod's cold counterpart. Strays drop them as blazes drop theirs, and any other monster killed in a snowy biome sometimes does." + BREAK
                        + "A frost rod grinds into frost powder, and frost powder, gunpowder and a snowball make ice charges.");
        this.add("manual." + Family.ID + ".chapter.staffs.power.title", "Power Staff");
        this.add("manual." + Family.ID + ".chapter.staffs.power",
                "Right click a block to push it one step away, or in a pull mode to drag it one step toward the side you clicked.");
    }
}
