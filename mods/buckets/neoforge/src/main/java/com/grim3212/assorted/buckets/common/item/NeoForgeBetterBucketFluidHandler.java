package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.buckets.BucketsCommonMod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Not a {@code FluidHandlerItemStack}, which stores fluid in a {@code SimpleFluidContent} component:
 * the bucket keeps it in its own tag, which the tooltip, milking, dispenser, model and Fabric side all read.
 */
public class NeoForgeBetterBucketFluidHandler implements IFluidHandlerItem {

    private final BetterBucketItem bucket;
    private final ItemStack onBroken;
    private final ItemStack empty;
    private final int capacity;
    private ItemStack container;

    public NeoForgeBetterBucketFluidHandler(ItemStack container, BetterBucketItem bucket, ItemStack onBroken, ItemStack empty, int capacity) {
        this.container = container;
        this.bucket = bucket;
        this.onBroken = onBroken;
        this.empty = empty;
        this.capacity = capacity;
    }

    @Override
    public ItemStack getContainer() {
        return this.container;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    private boolean isBucket() {
        return this.container.getCount() == 1 && this.container.is(this.bucket);
    }

    private FluidStack contained() {
        if (!this.isBucket()) {
            return FluidStack.EMPTY;
        }

        Fluid fluid = BetterBucketItem.getFluidFromString(BetterBucketItem.getFluid(this.container));
        int amount = BetterBucketItem.getAmount(this.container);
        return fluid == Fluids.EMPTY || amount <= 0 ? FluidStack.EMPTY : new FluidStack(fluid, amount);
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.contained();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity;
    }

    /**
     * Any real fluid this bucket's material stands the heat of, as long as the stack is still the
     * bucket this handler was built for.
     */
    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.isBucket() && !stack.isEmpty() && this.bucket.canHoldTemperatureOf(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !this.isFluidValid(0, resource)) {
            return 0;
        }

        FluidStack contained = this.contained();
        // One kind of fluid at a time.
        if (!contained.isEmpty() && !FluidStack.isSameFluidSameComponents(contained, resource)) {
            return 0;
        }

        int filled = roundToBuckets(Math.min(this.capacity - contained.getAmount(), resource.getAmount()));
        if (filled > 0 && action.execute()) {
            BetterBucketItem.storeFluid(this.container, resource.getFluid(), contained.getAmount() + filled);
        }
        return Math.max(filled, 0);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack contained = this.contained();
        if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(contained, resource)) {
            return FluidStack.EMPTY;
        }
        return this.drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack contained = this.contained();
        if (contained.isEmpty() || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        int drained = roundToBuckets(Math.min(contained.getAmount(), maxDrain));
        if (drained <= 0) {
            return FluidStack.EMPTY;
        }

        if (action.execute()) {
            int left = contained.getAmount() - drained;
            if (left > 0) {
                BetterBucketItem.setAmount(this.container, left);
            } else if (!this.onBroken.isEmpty()) {
                // Emptying a bucket that breaks on use hands back the broken remainder instead of an empty bucket.
                this.container = this.onBroken.copy();
            } else if (!this.empty.isEmpty()) {
                this.container = this.empty.copy();
            } else {
                BetterBucketItem.storeFluid(this.container, Fluids.EMPTY, 0);
            }
        }
        return contained.copyWithAmount(drained);
    }

    /** Unless partial amounts are enabled, these buckets move fluid a whole bucket at a time. */
    private static int roundToBuckets(int amount) {
        if (BucketsCommonMod.CONFIG.allowPartialBucketAmounts.get()) {
            return amount;
        }

        return amount - (amount % BetterBucketItem.getBucketAmount());
    }
}
