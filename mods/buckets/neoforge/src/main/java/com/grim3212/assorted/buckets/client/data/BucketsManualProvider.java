package com.grim3212.assorted.buckets.client.data;

import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.Family;
import com.grim3212.assorted.buckets.common.item.BucketsItems;
import com.grim3212.assorted.lib.data.LibManualProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

/**
 * This part's chapter of the Assorted Tools section, which every part shares; the explicit chapter orders keep the
 * section's order whichever parts are installed.
 */
public class BucketsManualProvider extends LibManualProvider {

    public BucketsManualProvider(PackOutput output) {
        super(output, Constants.MOD_ID, Family.ID);
    }

    @Override
    protected void addChapters() {
        this.section(Family.MANUAL_ORDER, Family.ICONS.toArray(Identifier[]::new));

        ChapterBuilder buckets = this.chapter("buckets", 110);
        buckets.recipes("buckets", BucketsItems.WOOD.bucket().get(), BucketsItems.STONE.bucket().get(), BucketsItems.GOLD.bucket().get(), BucketsItems.DIAMOND.bucket().get(), BucketsItems.NETHERITE.bucket().get()).every(50)
                .opensEveryItem(id -> id.getPath().endsWith("_bucket") && !id.getPath().endsWith("_milk_bucket"));
        // Milk is taken from an animal rather than crafted, so there is no recipe to draw.
        buckets.items("milk", BucketsItems.WOOD.milk().get(), BucketsItems.DIAMOND.milk().get(), BucketsItems.NETHERITE.milk().get())
                .every(50).opensEveryItem(id -> id.getPath().endsWith("_milk_bucket"));
    }
}
