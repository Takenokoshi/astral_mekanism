package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.jetbrains.annotations.NotNull;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class FluidFluidToFluidRecipe extends MekanismRecipe
		implements BiPredicate<@NotNull FluidStack, @NotNull FluidStack> {

	protected FluidFluidToFluidRecipe(ResourceLocation id, FluidStackIngredient inputFluidA,
			FluidStackIngredient inputFluidB, FluidStack outputFluid) {
		super(id);
		this.inputFluidA = inputFluidA;
		this.inputFluidB = inputFluidB;
		this.outputFluid = outputFluid.copy();
	}

	private final FluidStackIngredient inputFluidA;
	private final FluidStackIngredient inputFluidB;
	private final FluidStack outputFluid;

	@Override
	public boolean test(FluidStack input1, FluidStack input2) {
		return (inputFluidA.test(input1) && inputFluidB.test(input2));
	}

	public FluidStack getOutput(FluidStack input1, FluidStack input2) {
		return outputFluid.copy();
	}

	public FluidStackIngredient getInputA() {
		return inputFluidA;
	}

	public FluidStackIngredient getInputB() {
		return inputFluidB;
	}

	public List<FluidStack> getOutputDefinition(){
		return Collections.singletonList(outputFluid);
	}

	@Override
	public boolean isIncomplete(){
		return inputFluidA.hasNoMatchingInstances()||inputFluidB.hasNoMatchingInstances();
	}

	@Override
	public void logMissingTags(){
		inputFluidA.logMissingTags();
		inputFluidB.logMissingTags();
	}

	@Override
	public void write(FriendlyByteBuf buffer){
		inputFluidA.write(buffer);
		inputFluidB.write(buffer);
		outputFluid.writeToPacket(buffer);
	}
}
