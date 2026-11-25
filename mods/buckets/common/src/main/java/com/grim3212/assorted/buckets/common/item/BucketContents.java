package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.lib.core.item.IStackTooltipProvider;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

/**
 * The line a better bucket or milk bucket shows in its tooltip. The fluid itself stays in {@code
 * custom_data}, where {@link BetterBucketItem} keeps it; this only carries the capacity, which the tier config fixes at startup.
 *
 * @param capacity how many buckets it can hold
 */
public record BucketContents(int capacity) implements IStackTooltipProvider {

    public static final Codec<BucketContents> CODEC = Codec.INT.xmap(BucketContents::new, BucketContents::capacity);
    public static final StreamCodec<ByteBuf, BucketContents> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(BucketContents::new, BucketContents::capacity);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag flag, ItemStack stack) {
        int amount = BetterBucketItem.getAmount(stack);
        if (amount <= 0) {
            tooltip.accept(Component.translatable("tooltip.buckets.empty"));
        } else {
            tooltip.accept(Component.translatable("tooltip.buckets.contains", amount / BetterBucketItem.getBucketAmount(), this.capacity));
        }
    }
}
