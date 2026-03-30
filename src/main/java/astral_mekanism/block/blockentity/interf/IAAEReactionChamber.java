package astral_mekanism.block.blockentity.interf;

import java.util.List;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.generalrecipe.lookup.cache.recipe.AAEReactionRecipeCache;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeTypedLookupHandler;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;

public interface IAAEReactionChamber<BE extends BlockEntityRecipeMachine<ReactionChamberRecipe> & IAAEReactionChamber<BE>>
        extends IUnifiedRecipeTypedLookupHandler<ReactionChamberRecipe, AAEReactionRecipeCache>,
        IEnergyRequiredRecipeMachine {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

    default boolean containsItemByIndex(ItemStack stack, int index) {
        return getRecipeType().getInputCache().containsItemByIndex(getHandlerWorld(), stack, index);
    }

    default boolean containsFluid(FluidStack stack) {
        return getRecipeType().getInputCache().containsFluid(getHandlerWorld(), stack);
    }

    default boolean containsItemOtherByIndex(ItemStack itemStack, int index, ItemStack[] itemStacks,
            FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsItemOtherByIndex(getHandlerWorld(), itemStack, index, itemStacks,
                fluidStack);
    }

    default boolean containsFluidOther(ItemStack[] itemStacks, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsFluidOther(getHandlerWorld(), itemStacks, fluidStack);
    }

    default ReactionChamberRecipe findFirstRecipe(ItemStack[] itemStacks, FluidStack fluidStack) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), itemStacks, fluidStack);
    }

    default ReactionChamberRecipe findFirstRecipe(IInputHandler<ItemStack>[] itemHandlers,
            IInputHandler<FluidStack> fluidHandler) {
        ItemStack[] inputs = new ItemStack[itemHandlers.length];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = itemHandlers[i].getInput();
        }
        return findFirstRecipe(inputs, fluidHandler.getInput());
    }

    IExtendedFluidTank getInputTank();

    IExtendedFluidTank getOutputTank();

    MachineEnergyContainer<BE> getEnergyContainer();

    double getScaledProgress();
}
