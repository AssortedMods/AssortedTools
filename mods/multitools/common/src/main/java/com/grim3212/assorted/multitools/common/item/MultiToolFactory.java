package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;

/**
 * Makes each multitool. A loader that needs its own subclass, as NeoForge does to answer its item abilities, provides
 * one through {@code META-INF/services}; without one the multitool is a plain {@link MultiToolItem}.
 */
@FunctionalInterface
public interface MultiToolFactory {

    MultiToolItem create(ToolTier tier, float durabilityModifier, Item.Properties properties);
}
