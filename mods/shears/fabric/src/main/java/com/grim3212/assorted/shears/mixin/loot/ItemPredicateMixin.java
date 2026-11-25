package com.grim3212.assorted.shears.mixin.loot;

import com.grim3212.assorted.shears.common.item.ShearsMatch;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Only the item check is redirected, so the count and component checks still see the real stack.
 * Fabric only; NeoForge uses item abilities instead.
 */
@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {

    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/core/HolderSet;)Z"))
    private boolean assortedshears_shearsMatch(ItemStack stack, HolderSet<Item> items) {
        if (stack.is(items)) {
            return true;
        }

        boolean wantsShears = items.stream().anyMatch(holder -> holder.value() == Items.SHEARS);
        return wantsShears && ShearsMatch.isShears(stack);
    }
}
