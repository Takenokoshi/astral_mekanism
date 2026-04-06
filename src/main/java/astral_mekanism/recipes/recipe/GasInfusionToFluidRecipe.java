package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.InfusionStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class GasInfusionToFluidRecipe extends MekanismRecipe implements BiPredicate<GasStack, InfusionStack> {

    private final GasStackIngredient inputGas;
    private final InfusionStackIngredient inputInfusion;
    private final FluidStack output;

    protected GasInfusionToFluidRecipe(ResourceLocation id, GasStackIngredient inputGas,
            InfusionStackIngredient inputInfusion, FluidStack output) {
        super(id);
        this.inputGas = inputGas;
        this.inputInfusion = inputInfusion;
        this.output = output;
    }

    public GasStackIngredient getGasInput() {
        return inputGas;
    }

    public InfusionStackIngredient getInfusionInput() {
        return inputInfusion;
    }

    public List<FluidStack> getOutputDefinition() {
        return Collections.singletonList(output);
    }

    @Override
    public boolean test(GasStack t, InfusionStack u) {
        return inputGas.test(t) && inputInfusion.test(u);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputGas.write(buffer);
        inputInfusion.write(buffer);
        output.writeToPacket(buffer);
    }

    @Override
    public boolean isIncomplete() {
        return inputGas.hasNoMatchingInstances() || inputInfusion.hasNoMatchingInstances();
    }

    public FluidStack getOutput() {
        return output.copy();
    }

}
