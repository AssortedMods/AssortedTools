package com.grim3212.assorted.shears.mixin.loot;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
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

    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemInstance;is(Lnet/minecraft/core/HolderSet;)Z"))
    private boolean assortedshears_shearsMatch(ItemInstance stack, HolderSet<Item> items) {
        if (stack.is(items)) {
            return true;
        }

        // Tag only: ItemInstance has no item accessor for an instanceof ShearsItem check, but the common shears tag covers the same set via datagen.
        boolean wantsShears = items.contains(Items.SHEARS.builtInRegistryHolder());
        return wantsShears && stack.is(LibCommonTags.Items.SHEARS);
    }
}
