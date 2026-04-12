package astral_mekanism.recipe.builder;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import astral_mekanism.AMEConstants;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ReconstructionRecipeBuilder extends MekanismRecipeBuilder<ReconstructionRecipeBuilder> {

    private final ItemStackIngredient inputSolid;
    private final FluidStackIngredient inputFluid;
    private final GasStackIngredient inputGas;
    private FloatingLong energyRequired = FloatingLong.ZERO;
    private boolean itemNotConsumed = false;
    private final int duration;
    private final ItemStack outputItem;
    private final GasStack outputGas;

    protected ReconstructionRecipeBuilder(ItemStackIngredient inputSolid, FluidStackIngredient inputFluid,
            GasStackIngredient inputGas, int duration,
            ItemStack outputItem, GasStack outputGas) {
        super(AMEConstants.rl("reconstruction"));
        this.inputSolid = inputSolid;
        this.inputFluid = inputFluid;
        this.inputGas = inputGas;
        this.duration = duration;
        this.outputItem = outputItem;
        this.outputGas = outputGas;
    }

    public static ReconstructionRecipeBuilder reconstruction(ItemStackIngredient inputSolid,
            FluidStackIngredient inputFluid, GasStackIngredient inputGas, int duration,
            ItemStack outputItem, GasStack outputGas) {
        return new ReconstructionRecipeBuilder(inputSolid, inputFluid, inputGas, duration, outputItem, outputGas);
    }

    public ReconstructionRecipeBuilder energyRequired(FloatingLong energyRequired) {
        this.energyRequired = energyRequired;
        return this;
    }

    public ReconstructionRecipeBuilder setItemNotConsumed(boolean value) {
        this.itemNotConsumed = value;
        return this;
    }

    @Override
    protected MekanismRecipeBuilder<ReconstructionRecipeBuilder>.RecipeResult getResult(ResourceLocation id) {
        return new ReconstructionRecipeResult(id);
    }

    public class ReconstructionRecipeResult extends RecipeResult {

        protected ReconstructionRecipeResult(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add(JsonConstants.ITEM_INPUT, inputSolid.serialize());
            json.add(JsonConstants.FLUID_INPUT, inputFluid.serialize());
            json.add(JsonConstants.GAS_INPUT, inputGas.serialize());
            if (!energyRequired.isZero()) {
                // Only add energy required if it is not zero, as otherwise it will default to
                // zero
                json.addProperty(JsonConstants.ENERGY_REQUIRED, energyRequired);
            }
            json.addProperty("itemNotConsumed", itemNotConsumed);
            json.addProperty(JsonConstants.DURATION, duration);
            if (!outputItem.isEmpty()) {
                json.add(JsonConstants.ITEM_OUTPUT, SerializerHelper.serializeItemStack(outputItem));
            }
            if (!outputGas.isEmpty()) {
                json.add(JsonConstants.GAS_OUTPUT, SerializerHelper.serializeGasStack(outputGas));
            }
        }
    }

}
