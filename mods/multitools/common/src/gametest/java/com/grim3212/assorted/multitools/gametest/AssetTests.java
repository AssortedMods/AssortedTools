package com.grim3212.assorted.multitools.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.Family;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.multitools.gametest.MultitoolsTestSupport.*;

/** What the mod ships: a model and a name for every item, a name for every entity and tag, and recipes that load. */
final class AssetTests {

    private AssetTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_item_has_a_model_and_a_name", AssetTests::everyItemHasAModelAndAName);
        out.accept("every_item_tag_has_a_name", AssetTests::everyItemTagHasAName);
        out.accept("every_recipe_loads_or_is_conditioned_off", AssetTests::everyRecipeLoadsOrIsConditionedOff);
    }

    private static void everyItemHasAModelAndAName(GameTestHelper helper) {
        JsonObject lang = readLang(helper);
        List<String> missing = new ArrayList<>();
        int items = 0;

        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            Identifier id = entry.getKey().identifier();
            if (!Constants.MOD_ID.equals(id.getNamespace())) {
                continue;
            }

            items++;
            if (!resourceExists("/assets/" + id.getNamespace() + "/items/" + id.getPath() + ".json")) {
                missing.add("items/" + id.getPath() + ".json");
            }
            if (!(entry.getValue() instanceof BlockItem) && !resourceExists("/assets/" + id.getNamespace() + "/textures/item/" + id.getPath() + ".png")) {
                missing.add("textures/item/" + id.getPath() + ".png");
            }
            if (!lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
            if (Constants.MOD_ID.equals(entry.getKey().identifier().getNamespace()) && !lang.has(entry.getValue().getDescriptionId())) {
                missing.add("lang key " + entry.getValue().getDescriptionId());
            }
        }

        if (!lang.has("itemGroup." + Family.ID)) {
            missing.add("lang key itemGroup." + Family.ID);
        }

        helper.assertTrue(items > 0, "no items are registered under " + Constants.MOD_ID + ", so nothing was checked");
        helper.assertTrue(missing.isEmpty(), missing.size() + " missing assets: " + String.join("; ", missing));
        helper.succeed();
    }

    /**
     * Every non-vanilla item tag has a {@code tag.item.<namespace>.<path>} name, the check Fabric
     * API warns about at dev startup. Both loaders name the standard c: tags, so anything missing is ours.
     */
    private static void everyItemTagHasAName(GameTestHelper helper) {
        Language language = Language.getInstance();
        List<String> missing = helper.getLevel().registryAccess().lookupOrThrow(Registries.ITEM).getTags()
                .map(tag -> tag.key().location())
                .filter(id -> !"minecraft".equals(id.getNamespace()))
                .map(id -> "tag.item." + id.getNamespace() + "." + id.getPath().replace('/', '.'))
                .filter(key -> !language.has(key))
                .sorted()
                .toList();
        helper.assertTrue(missing.isEmpty(), "item tags with no name in any lang file: " + missing);
        helper.succeed();
    }

    private static void everyRecipeLoadsOrIsConditionedOff(GameTestHelper helper) {
        MinecraftServer server = helper.getLevel().getServer();
        FileToIdConverter recipes = FileToIdConverter.json("recipe");
        String conditionsKey = Services.PLATFORM.getPlatformName().equals("Fabric") ? "fabric:load_conditions" : "neoforge:conditions";
        List<String> failed = new ArrayList<>();

        recipes.listMatchingResources(server.getResourceManager()).forEach((file, resource) -> {
            Identifier id = recipes.fileToId(file);
            if (!id.getNamespace().equals(Constants.MOD_ID) || server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id)).isPresent()) {
                return;
            }

            try (BufferedReader reader = resource.openAsReader()) {
                if (!JsonParser.parseReader(reader).getAsJsonObject().has(conditionsKey)) {
                    failed.add(id.toString());
                }
            } catch (IOException e) {
                failed.add(id + " (" + e.getMessage() + ")");
            }
        });

        helper.assertTrue(failed.isEmpty(), failed.size() + " recipes failed to load without being conditioned off: " + String.join(", ", failed));
        helper.succeed();
    }
}
