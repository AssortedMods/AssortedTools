package com.grim3212.assorted.ultimatefist.config;

import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.ultimatefist.Constants;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class UltimateFistConfig {

    /** The fist's own material, which none of the shared tiers come near. */
    public final ToolTier ultimateItemTier;

    public UltimateFistConfig() {
        // Needed at registration: the fist bakes its durability and damage in as it is constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");

        this.ultimateItemTier = new ToolTier(builder, "ultimate_fist", "ultimate", new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1561, 64F, 64F, 0, LibCommonTags.Items.NETHER_STARS), 0F, 0F);

        builder.setup();
    }
}
