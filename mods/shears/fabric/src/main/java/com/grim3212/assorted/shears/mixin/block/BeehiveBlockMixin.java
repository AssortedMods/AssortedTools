package com.grim3212.assorted.shears.mixin.block;

import com.grim3212.assorted.shears.common.item.ShearsMatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Redirects vanilla's {@code is(Items.SHEARS)} rather than copying the branch, so it stays in step
 * with vanilla; NeoForge already patches this line to {@code canPerformAction(SHEARS_HARVEST)}.
 */
@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {

    // Target is(Object), not is(Item) - it erases that way and is(Item) fails at load; ordinal 0 is the shears branch, ordinal 1 the glass bottle.
    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean assortedshears_shearsHarvest(ItemStack stack, Object item) {
        return ShearsMatch.matches(stack, item);
    }
}
