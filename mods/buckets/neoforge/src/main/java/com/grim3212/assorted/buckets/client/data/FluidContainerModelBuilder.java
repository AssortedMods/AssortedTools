package com.grim3212.assorted.buckets.client.data;

import com.google.gson.JsonObject;
import com.grim3212.assorted.buckets.client.model.fluidcontainer.FluidContainerModel;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Writes the {@link FluidContainerModel} loader's options into a model json. */
public class FluidContainerModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private boolean flipGas = false;
    private boolean coverIsMask = true;
    private boolean applyFluidLuminosity = true;

    protected FluidContainerModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(FluidContainerModel.LOADER_NAME, parent, existingFileHelper, false);
    }

    public static <T extends ModelBuilder<T>> FluidContainerModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new FluidContainerModelBuilder<>(parent, existingFileHelper);
    }

    /** Turn the model upside down when the fluid is lighter than air. */
    public FluidContainerModelBuilder<T> flipGas(boolean flip) {
        this.flipGas = flip;
        return this;
    }

    /** Fill the cover's shape with the base texture, which is how a bucket draws its own lip back over the fluid. */
    public FluidContainerModelBuilder<T> coverIsMask(boolean isMask) {
        this.coverIsMask = isMask;
        return this;
    }

    /** Draw the fluid layer fullbright when the fluid emits light. */
    public FluidContainerModelBuilder<T> applyFluidLuminosity(boolean apply) {
        this.applyFluidLuminosity = apply;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);
        json.addProperty("flip_gas", this.flipGas);
        json.addProperty("cover_is_mask", this.coverIsMask);
        json.addProperty("apply_fluid_luminosity", this.applyFluidLuminosity);
        return json;
    }
}
