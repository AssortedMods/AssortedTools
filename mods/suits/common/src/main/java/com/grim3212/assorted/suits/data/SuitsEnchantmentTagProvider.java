package com.grim3212.assorted.suits.data;

import com.grim3212.assorted.lib.data.LibEnchantmentTagProvider;
import com.grim3212.assorted.suits.common.enchantment.SuitsEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Chicken jump is obtainable at a table, in loot and by trade. */
public class SuitsEnchantmentTagProvider extends LibEnchantmentTagProvider {

    public SuitsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        obtainable(List.of(SuitsEnchantments.CHICKEN_JUMP));
    }
}
