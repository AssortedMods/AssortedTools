package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.lib.annotations.LoaderImplement;
import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.buckets.config.BucketConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * A milk bucket of one of the materials. Vanilla's milk bucket class does the drinking: the
 * animation and duration, the stat and advancement, and clearing effects the way each loader does.
 */
public class BetterMilkBucketItem extends MilkBucketItem implements ITiered {

    private final Supplier<BetterBucketItem> parent;

    /**
     * @param bucket the parent's bucket settings, for the capacity in its tooltip; passed in because the
     *               parent is not read while items are still being registered
     */
    public BetterMilkBucketItem(Supplier<BetterBucketItem> parent, BucketConfig bucket, Properties props) {
        super(props.stacksTo(1).component(BucketsDataComponents.BUCKET_CONTENTS.get(), new BucketContents(bucket.getMaxBuckets())));
        this.parent = parent;
    }

    public BetterBucketItem getParent() {
        return parent.get();
    }

    @Override
    public ToolTier getToolTier() {
        return this.getParent().getToolTier();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        // Handing super a copy keeps the effect clear, stat and advancement it drives, while the bucket only loses one bucket of milk instead of being swapped for an empty vanilla one.
        super.finishUsingItem(stack.copy(), worldIn, entityLiving);

        if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
            int amount = BetterBucketItem.getAmount(stack);
            BetterBucketItem.setAmount(stack, amount - getBucketAmount());
        }

        return this.getParent().tryBreakBucket(stack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return !BetterBucketItem.craftingRemainder(this.getParent(), stack).isEmpty();
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FORGE, value = "IItemExtension")
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return BetterBucketItem.craftingRemainder(this.getParent(), stack);
    }

    @LoaderImplement(loader = LoaderImplement.Loader.FABRIC, value = "FabricItem")
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return BetterBucketItem.craftingRemainder(this.getParent(), stack);
    }

    public int getBucketAmount() {
        //Currently set to an int might change to a long
        return (int) Services.FLUIDS.getBucketAmount();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        // Don't show if the bucket is empty
        if (BetterBucketItem.getAmount(stack) <= 0)
            return false;
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        // Get remainder calculations from stored and maxAmount
        int reversedAmount = this.getParent().getMaximumMillibuckets() - BetterBucketItem.getAmount(stack);
        return Math.round(13.0F - (float) reversedAmount * 13.0F / (float) this.getParent().getMaximumMillibuckets());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (float) BetterBucketItem.getAmount(stack) / (float) this.getParent().getMaximumMillibuckets());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
