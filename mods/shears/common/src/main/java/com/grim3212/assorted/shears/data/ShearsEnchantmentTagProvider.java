package com.grim3212.assorted.shears.data;

import com.grim3212.assorted.lib.data.LibEnchantmentTagProvider;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Coral Cutter turns up at the table, in trades and in loot. */
public class ShearsEnchantmentTagProvider extends LibEnchantmentTagProvider {

    public ShearsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        obtainable(List.of(ShearsEnchantments.CORAL_CUTTER));
    }
}
