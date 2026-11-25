package com.grim3212.assorted.throwingspears.common.entity;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.throwingspears.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ThrowingSpearsEntities {

    public static final RegistryProvider<EntityType<?>> ENTITIES = RegistryProvider.create(Registries.ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<EntityType<BetterSpearEntity>> BETTER_SPEAR = ENTITIES.register("better_spear", () -> EntityType.Builder.<BetterSpearEntity>of(BetterSpearEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "better_spear").toString()));

    public static void init() {
    }
}
