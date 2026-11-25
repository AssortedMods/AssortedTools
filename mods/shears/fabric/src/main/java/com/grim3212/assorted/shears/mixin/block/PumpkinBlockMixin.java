package com.grim3212.assorted.shears.mixin.block;

import com.grim3212.assorted.shears.common.item.ShearsMatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * See {@link BeehiveBlockMixin} for why this redirects vanilla's hardcoded shears check rather than
 * copying the branch, and why it's Fabric only.
 */
@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin {

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean assortedshears_shearsCarve(ItemStack stack, Item item) {
        return ShearsMatch.matches(stack, item);
    }
}
