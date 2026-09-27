package com.grim3212.assorted.shears.mixin.block;

import com.grim3212.assorted.shears.common.item.ShearsMatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TripWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Lets the mod's shears disarm a tripwire on Fabric. See {@link BeehiveBlockMixin} for why it is a
 * redirect and Fabric only.
 */
@Mixin(TripWireBlock.class)
public abstract class TripWireBlockMixin {

    // Target is(Object), not is(Item) - it erases that way and is(Item) fails at load.
    @Redirect(method = "playerWillDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean assortedshears_shearsDisarm(ItemStack stack, Object item) {
        return ShearsMatch.matches(stack, item);
    }
}
