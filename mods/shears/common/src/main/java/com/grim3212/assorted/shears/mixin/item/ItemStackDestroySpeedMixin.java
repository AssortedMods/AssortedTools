package com.grim3212.assorted.shears.mixin.item;

import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The {@code minecraft:tool} component is static and can't depend on an enchantment, so a mixin on
 * {@link ItemStack#getDestroySpeed(BlockState)} handles it instead, covering vanilla shears too.
 */
@Mixin(ItemStack.class)
public class ItemStackDestroySpeedMixin {

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void assortedshears_coralCutterSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof ShearsItem && state.is(ShearsTags.ALL_CORALS) && ShearsEnchantments.hasCoralCutter(stack)) {
            cir.setReturnValue(10.0F);
        }
    }
}
