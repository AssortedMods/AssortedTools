package com.grim3212.assorted.buckets.client.data;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.common.item.BucketPair;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.data.LibLanguageProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

/**
 * The en_us.json of this mod. An item whose name is its id in title case needs no line here; these are the names
 * that read differently, the lines every Assorted Tools part shares, and this part's manual chapter.
 */
public class BucketsLanguageProvider extends LibLanguageProvider {

    private static final String BREAK = "\n\n";

    public BucketsLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void addNames() {
        this.add("itemGroup." + Constants.FAMILY_ID, "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".title", "Assorted Tools");
        this.add("manual." + Constants.FAMILY_ID + ".description",
                "Tool and armor sets for every material, buckets that hold more than one, throwing weapons "
                        + "and the wands that do the digging for you.");

        this.add("tooltip.buckets.empty", "Empty");
        this.add("tooltip.buckets.contains", "Contains %s/%s Buckets");

        // A bucket names its filled form through a key of its own rather than an item.
        for (BucketPair pair : BucketsItems.pairs()) {
            this.add(pair.bucket().get().getDescriptionId() + "_filled", material(pair.tier().getName()) + " %s Bucket");
        }
        this.nameItems("wood_(.+)", m -> "Wooden " + titleCase(m.group(1)));
        this.nameItems("gold_(.+)", m -> "Golden " + titleCase(m.group(1)));

        this.add("manual." + Constants.FAMILY_ID + ".chapter.buckets", "Buckets");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.buckets.buckets.title", "Better Buckets");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.buckets.buckets",
                "These buckets hold more than one bucket of what is in them, and the material decides how "
                        + "many." + BREAK
                        + "Some materials are very weak and are destroyed when they are emptied. " + BREAK
                        + "Hotter fluids need a sturdier bucket, so what a bucket can pick up is a question of "
                        + "material as well.");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.buckets.milk.title", "Milking");
        this.add("manual." + Constants.FAMILY_ID + ".chapter.buckets.milk",
                "Any of these buckets can be used to milk cows." + BREAK
                        + "What you can milk depends on the bucket. All of them can manage a cow, better ones "
                        + "also manage a sheep, and the best of them a pig as well.");
    }

    /** The name a tool material reads as: the vanilla tiers use the adjective, the rest their id. */
    private static String material(String id) {
        return switch (id) {
            case "wood" -> "Wooden";
            case "gold" -> "Golden";
            default -> titleCase(id);
        };
    }
}
