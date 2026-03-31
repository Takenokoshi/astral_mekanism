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

    default boolean containsItem(ItemStack stack) {
        return getRecipeType().getInputCache().containsItem(getHandlerWorld(), stack);
    }

    default boolean containsFluid(FluidStack stack) {
        return getRecipeType().getInputCache().containsFluid(getHandlerWorld(), stack);
    }

    default boolean containsItemOther(ItemStack itemStack, List<ItemStack> itemStacks, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsItemOther(getHandlerWorld(), itemStack, itemStacks, fluidStack);
    }

    default boolean containsFluidOther(List<ItemStack> itemStacks, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsFluidOther(getHandlerWorld(), itemStacks, fluidStack);
    }

    default ReactionChamberRecipe findFirstRecipe(List<ItemStack> itemStacks, FluidStack fluidStack) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), itemStacks, fluidStack);
    }

    default ReactionChamberRecipe findFirstRecipe(List<IInputHandler<ItemStack>> itemHandlers,
            IInputHandler<FluidStack> fluidHandler) {
        return findFirstRecipe(itemHandlers.stream()
                .map(IInputHandler<ItemStack>::getInput)
                .filter(stack -> !stack.isEmpty())
                .toList(), fluidHandler.getInput());
    }

    IExtendedFluidTank getInputTank();

    IExtendedFluidTank getOutputTank();

    MachineEnergyContainer<BE> getEnergyContainer();

    double getScaledProgress();
}
