package com.grim3212.assorted.pokeball.common.entity;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.pokeball.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class PokeballEntities {

    public static final RegistryProvider<EntityType<?>> ENTITIES = RegistryProvider.create(Registries.ENTITY_TYPE, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<EntityType<PokeballEntity>> POKEBALL = ENTITIES.register("pokeball", () -> EntityType.Builder.<PokeballEntity>of(PokeballEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(32).updateInterval(1)
            .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "pokeball"))));

    public static void init() {
    }
}
