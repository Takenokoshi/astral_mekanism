package astral_mekanism.recipes.lookup;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.recipes.inputRecipeCache.AstralCraftingRecipeCache;
import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.IRecipeTypedLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public interface AstralCraftingRecipeLookUpHandler
        extends IRecipeTypedLookupHandler<AstralCraftingRecipe, AstralCraftingRecipeCache> {

    default boolean containsInputItem(@Nullable Level world, ItemStack input, int index) {
        return this.getRecipeType().getInputCache().containsInputItem(world, input, index);
    }

    default boolean containsInputFluid(@Nullable Level world, FluidStack input) {
        return this.getRecipeType().getInputCache().containsInputFluid(world, input);
    }

    default boolean containsInputGas(@Nullable Level world, GasStack input) {
        return this.getRecipeType().getInputCache().containsInputGas(world, input);
    }

    default boolean containsInputGas(@Nullable Level world, Gas input) {
        return this.getRecipeType().getInputCache().containsInputGas(world, input);
    }

    default boolean containsInputItemOther(@Nullable Level world, ItemStack input, int index,
            ItemStack[] inputItems, FluidStack inputFluid, GasStack inputGas) {
        return this.getRecipeType().getInputCache().containsInputItemOther(world, input, index, inputItems, inputFluid,
                inputGas);
    }

    default boolean containsInputFluidOther(@Nullable Level world, FluidStack input,
            ItemStack[] inputItems, GasStack inputGas) {
        return this.getRecipeType().getInputCache().containsInputFluidOther(world, input, inputItems, inputGas);
    }

    default boolean containsInputGasOther(@Nullable Level world, GasStack input,
            ItemStack[] inputItems, FluidStack inputFluid) {
        return this.getRecipeType().getInputCache().containsInputGasOther(world, input, inputItems, inputFluid);
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
