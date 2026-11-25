package com.grim3212.assorted.shears.mixin.block;

import com.grim3212.assorted.shears.common.item.ShearsMatch;
import net.minecraft.world.item.Item;
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

    // Ordinal 0 is the shears branch, ordinal 1 the glass bottle.
    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean assortedshears_shearsHarvest(ItemStack stack, Item item) {
        return ShearsMatch.matches(stack, item);
    }
}
