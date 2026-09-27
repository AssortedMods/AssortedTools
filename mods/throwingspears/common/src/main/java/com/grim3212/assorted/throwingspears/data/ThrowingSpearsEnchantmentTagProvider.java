package com.grim3212.assorted.throwingspears.data;

import com.grim3212.assorted.lib.data.LibEnchantmentTagProvider;
import com.grim3212.assorted.throwingspears.common.enchantment.ThrowingSpearsEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Every enchantment is obtainable. */
public class ThrowingSpearsEnchantmentTagProvider extends LibEnchantmentTagProvider {

    public ThrowingSpearsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        obtainable(List.of(ThrowingSpearsEnchantments.BOUNCINESS, ThrowingSpearsEnchantments.CONDUCTIVE, ThrowingSpearsEnchantments.FLAMMABLE, ThrowingSpearsEnchantments.UNSTABLE));
    }
}
