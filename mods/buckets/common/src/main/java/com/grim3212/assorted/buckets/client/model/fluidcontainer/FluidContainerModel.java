package com.grim3212.assorted.buckets.client.model.fluidcontainer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.lib.client.model.QuadTransformers;
import com.grim3212.assorted.lib.client.model.UnbakedGeometryHelper;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecification;
import com.grim3212.assorted.lib.client.model.loaders.IModelSpecificationLoader;
import com.grim3212.assorted.lib.client.model.loaders.context.IModelBakingContext;
import com.grim3212.assorted.lib.client.model.state.SimpleModelState;
import com.grim3212.assorted.lib.core.fluid.FluidInformation;
import com.grim3212.assorted.lib.core.fluid.IFluidVariantHandler;
import com.grim3212.assorted.lib.platform.ClientServices;
import com.grim3212.assorted.lib.platform.Services;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A bucket that draws whatever fluid it holds, including other mods'. The json names the {@code base},
 * {@code fluid} (the shape the fluid fills), {@code cover} and {@code particle} textures; the model bakes
 * empty, and its overrides bake a model for each fluid the first time a stack holding it is drawn, as
 * the fluids are only known once the game is running.
 */
public class FluidContainerModel implements IModelSpecification<FluidContainerModel> {

    public static final ResourceLocation LOADER_NAME = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fluid_container");

    /** The tint index of the fluid layer, which {@code BucketsClient}'s item colour paints with the fluid's colour. */
    public static final int FLUID_TINT_INDEX = 1;

    // Depth offsets to prevent Z-fighting
    private static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.002f), new Quaternionf());
    private static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.004f), new Quaternionf());

    private final boolean flipGas;
    private final boolean coverIsMask;
    private final boolean applyFluidLuminosity;

    private FluidContainerModel(boolean flipGas, boolean coverIsMask, boolean applyFluidLuminosity) {
        this.flipGas = flipGas;
        this.coverIsMask = coverIsMask;
        this.applyFluidLuminosity = applyFluidLuminosity;
    }

    @Override
    public BakedModel bake(IModelBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        Layers layers = new Layers(context.getMaterial("base").orElse(null), context.getMaterial("fluid").orElse(null),
                context.getMaterial("cover").orElse(null), context.getMaterial("particle").orElse(null), context.getTransforms());
        return new ContainedFluidOverrides(baker, this, layers).bakeFor(Fluids.EMPTY, spriteGetter);
    }

    /**
     * The bucket holding {@code fluid}: its own texture, the fluid cut to the {@code fluid} mask, then the
     * {@code cover} drawn back over the fluid. Every layer is on the block atlas, which items share on
     * 1.21.1, so the whole model is one quad list drawn translucent.
     */
    private BakedModel bake(Layers layers, Fluid fluid, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides) {
        FluidInformation fluidInformation = new FluidInformation(fluid);
        Optional<IFluidVariantHandler> fluidVariant = Services.FLUIDS.getVariantHandlerFor(fluidInformation);

        TextureAtlasSprite baseSprite = layers.base() != null ? spriteGetter.apply(layers.base()) : null;
        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? ClientServices.FLUIDS.getSprite(fluidInformation) : null;
        TextureAtlasSprite coverSprite = (layers.cover() != null && (!this.coverIsMask || layers.base() != null)) ? spriteGetter.apply(layers.cover()) : null;

        TextureAtlasSprite particleSprite = layers.particle() != null ? spriteGetter.apply(layers.particle()) : null;
        if (particleSprite == null) particleSprite = fluidSprite;
        if (particleSprite == null) particleSprite = baseSprite;
        if (particleSprite == null && !this.coverIsMask) particleSprite = coverSprite;

        // If the fluid is lighter than air, rotate 180deg to turn it upside down
        ModelState modelState = BlockModelRotation.X0_Y0;
        if (this.flipGas && fluidSprite != null && fluidVariant.isPresent() && fluidVariant.get().getDensity(fluidInformation) < 0) {
            modelState = new SimpleModelState(modelState.getRotation().compose(new Transformation(null, new Quaternionf(0, 0, 1, 0), null, null)));
        }

        List<BakedQuad> quads = new ArrayList<>();

        if (baseSprite != null) {
            quads.addAll(bakeGeneratedItem(layers.base(), baseSprite, spriteGetter, modelState));
        }

        if (layers.fluid() != null && fluidSprite != null) {
            TextureAtlasSprite mask = spriteGetter.apply(layers.fluid());
            List<BakedQuad> fluidQuads = bakeMasked(mask, fluidSprite, FLUID_TINT_INDEX, new SimpleModelState(modelState.getRotation().compose(FLUID_TRANSFORM)));

            if (this.applyFluidLuminosity && fluidVariant.isPresent() && fluidVariant.get().getLuminance(fluidInformation) > 0) {
                fluidQuads = QuadTransformers.settingMaxEmissivity().process(fluidQuads);
            }
            quads.addAll(fluidQuads);
        }

        if (coverSprite != null) {
            // The cover is always the mask; it's filled with the base texture when coverIsMask (the bucket's own lip drawn back over the fluid), or the cover texture itself otherwise.
            TextureAtlasSprite fill = this.coverIsMask ? baseSprite : coverSprite;
            quads.addAll(bakeMasked(coverSprite, fill, -1, new SimpleModelState(modelState.getRotation().compose(COVER_TRANSFORM))));
        }

        return new Baked(List.copyOf(quads), particleSprite, layers.transforms(), overrides);
    }

    /**
     * Vanilla's generated-item shape, the extruded edges included, for a single {@code layer0}. Its
     * generator only works from a model, so it is handed one holding nothing but that texture.
     */
    // NeoForge deprecates BlockModel#getElements for its geometry-aware lookup, which a generated model without custom geometry answers the same.
    @SuppressWarnings("deprecation")
    private static List<BakedQuad> bakeGeneratedItem(Material layer0, TextureAtlasSprite sprite, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
        BlockModel textures = new BlockModel(null, List.of(), Map.of("layer0", Either.left(layer0)), null, null, ItemTransforms.NO_TRANSFORMS, List.of());
        List<BakedQuad> quads = new ArrayList<>();
        for (BlockElement element : new ItemModelGenerator().generateBlockModel(spriteGetter, textures).getElements()) {
            for (Map.Entry<Direction, BlockElementFace> face : element.faces.entrySet()) {
                quads.add(UnbakedGeometryHelper.bakeElementFace(element, face.getValue(), sprite, face.getKey(), modelState));
            }
        }
        return quads;
    }

    /** Cuts {@code mask}'s opaque pixels out as flat elements and bakes them with {@code fill}'s texture. */
    private static List<BakedQuad> bakeMasked(TextureAtlasSprite mask, TextureAtlasSprite fill, int tintIndex, ModelState modelState) {
        List<BakedQuad> quads = new ArrayList<>();
        for (BlockElement element : UnbakedGeometryHelper.createUnbakedItemMaskElements(tintIndex, mask)) {
            for (Map.Entry<Direction, BlockElementFace> face : element.faces.entrySet()) {
                quads.add(UnbakedGeometryHelper.bakeElementFace(element, face.getValue(), fill, face.getKey(), modelState));
            }
        }
        return quads;
    }

    /** What the json named, kept for the per fluid bakes. */
    private record Layers(@Nullable Material base, @Nullable Material fluid, @Nullable Material cover, @Nullable Material particle, ItemTransforms transforms) {
    }

    public enum Loader implements IModelSpecificationLoader<FluidContainerModel> {
        INSTANCE;

        @Override
        public FluidContainerModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            boolean flip = GsonHelper.getAsBoolean(modelContents, "flip_gas", false);
            boolean coverIsMask = GsonHelper.getAsBoolean(modelContents, "cover_is_mask", true);
            boolean applyFluidLuminosity = GsonHelper.getAsBoolean(modelContents, "apply_fluid_luminosity", true);
            return new FluidContainerModel(flip, coverIsMask, applyFluidLuminosity);
        }
    }

    /**
     * Picks the model for the fluid a stack holds, baking it the first time. The cache lives as long as
     * the baked model, which a resource reload replaces, so nothing in it goes stale.
     */
    private static final class ContainedFluidOverrides extends ItemOverrides {

        private final FluidContainerModel model;
        private final Layers layers;
        private final Map<Fluid, BakedModel> byFluid = new ConcurrentHashMap<>();

        // The public constructor is the only one vanilla has; NeoForge deprecates it in favour of an
        // overload taking a sprite getter that vanilla, which this module builds against, does not have.
        // With no overrides to bake, neither reads the model or the baker beyond that.
        @SuppressWarnings("deprecation")
        private ContainedFluidOverrides(ModelBaker baker, FluidContainerModel model, Layers layers) {
            super(baker, null, List.of());
            this.model = model;
            this.layers = layers;
        }

        private BakedModel bakeFor(Fluid fluid, Function<Material, TextureAtlasSprite> spriteGetter) {
            return this.byFluid.computeIfAbsent(fluid, key -> this.model.bake(this.layers, key, spriteGetter, this));
        }

        @Override
        public @Nullable BakedModel resolve(BakedModel original, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            Fluid fluid = Services.FLUIDS.get(stack).map(FluidInformation::fluid).orElse(Fluids.EMPTY);
            // Past baking, a material is looked up on its stitched atlas.
            return this.bakeFor(fluid, Material::sprite);
        }
    }

    /** One bucket holding one fluid, drawn flat lit like any generated item. */
    private record Baked(List<BakedQuad> quads, TextureAtlasSprite particle, ItemTransforms transforms, ItemOverrides overrides) implements BakedModel {

        // NeoForge deprecates this vanilla method for its ModelData overload, which an item model never needs.
        @SuppressWarnings("deprecation")
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
            return side == null ? this.quads : List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        // NeoForge deprecates this vanilla method for its ModelData overload, which an item model never needs.
        @SuppressWarnings("deprecation")
        @Override
        public TextureAtlasSprite getParticleIcon() {
            return this.particle;
        }

        // NeoForge deprecates this vanilla method for applyTransform, whose default reads it.
        @SuppressWarnings("deprecation")
        @Override
        public ItemTransforms getTransforms() {
            return this.transforms;
        }

        @Override
        public ItemOverrides getOverrides() {
            return this.overrides;
        }
    }
}
