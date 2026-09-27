package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibEnchantmentTagProvider;
import com.grim3212.assorted.tools.common.enchantment.ToolsEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Every enchantment is obtainable. */
public class ToolsEnchantmentTagProvider extends LibEnchantmentTagProvider {

    public ToolsEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        obtainable(List.of(
                ToolsEnchantments.CHICKEN_JUMP, ToolsEnchantments.BOUNCINESS, ToolsEnchantments.CONDUCTIVE,
                ToolsEnchantments.FLAMMABLE, ToolsEnchantments.UNSTABLE, ToolsEnchantments.CORAL_CUTTER));
    }
}
