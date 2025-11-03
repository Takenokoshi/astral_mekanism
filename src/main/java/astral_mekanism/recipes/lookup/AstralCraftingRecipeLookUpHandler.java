package astral_mekanism.recipes.lookup;

import java.util.Arrays;

import astral_mekanism.recipes.inputRecipeCache.AstralCraftingRecipeCache;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.IRecipeTypedLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface AstralCraftingRecipeLookUpHandler
        extends IRecipeTypedLookupHandler<AstralCraftingRecipe, AstralCraftingRecipeCache> {

    default boolean containsInputItem(ItemStack input, int index) {
        return this.getRecipeType().getInputCache().containsInputItem(this.getHandlerWorld(), input, index);
    }

    default boolean containsInputFluid(FluidStack input) {
        return this.getRecipeType().getInputCache().containsInputFluid(this.getHandlerWorld(), input);
    }

    default boolean containsInputGas(GasStack input) {
        return this.getRecipeType().getInputCache().containsInputGas(this.getHandlerWorld(), input);
    }

    default boolean containsInputGas(Gas input) {
        return this.getRecipeType().getInputCache().containsInputGas(this.getHandlerWorld(), input);
    }

    default boolean containsInputItemOther(ItemStack input, int index,
            ItemStack[] inputItems, FluidStack inputFluid, GasStack inputGas) {
        return this.getRecipeType().getInputCache().containsInputItemOther(this.getHandlerWorld(), input, index,
                inputItems, inputFluid,
                inputGas);
    }

    default boolean containsInputFluidOther(FluidStack input,
            ItemStack[] inputItems, GasStack inputGas) {
        return this.getRecipeType().getInputCache().containsInputFluidOther(this.getHandlerWorld(), input, inputItems,
                inputGas);
    }

    default boolean containsInputGasOther(GasStack input,
            ItemStack[] inputItems, FluidStack inputFluid) {
        return this.getRecipeType().getInputCache().containsInputGasOther(this.getHandlerWorld(), input, inputItems,
                inputFluid);
    }

    default boolean containsInputGasOther(Gas input,
            ItemStack[] inputItems, FluidStack inputFluid) {
        return this.getRecipeType().getInputCache().containsInputGasOther(this.getHandlerWorld(), input.getStack(1),
                inputItems, inputFluid);
    }

    default AstralCraftingRecipe findFirstRecipe(ItemStack[] inputItems, FluidStack inputFluid,
            GasStack inputGas) {
        return this.getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), inputItems, inputFluid,
                inputGas);
    }

    default AstralCraftingRecipe findFirstRecipe(IInputHandler<ItemStack>[] itemInputHandlers,
            IInputHandler<FluidStack> fluidInputHandler, IInputHandler<GasStack> gasInputHandler) {
        return this.findFirstRecipe(Arrays.stream(itemInputHandlers).map(i -> i.getInput()).toArray(ItemStack[]::new),
                fluidInputHandler.getInput(), gasInputHandler.getInput());
    }

}
