package astral_mekanism.block.blockentity.interf;

import java.util.List;

import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.lookup.cache.recipe.CropSoilInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeTypedLookupHandler;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IGreenHouse<BE extends TileEntityConfigurableMachine & IGreenHouse<BE>>
        extends IUnifiedRecipeTypedLookupHandler<CropSoilRecipe, CropSoilInputRecipeCache> {
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    default IUnifiedRecipeTypeProvider<CropSoilRecipe, CropSoilInputRecipeCache> getRecipeType() {
        return GeneralRecipeType.CROP_SOIL;
    }

    default boolean containsRecipeCrop(ItemStack stack) {
        return getRecipeType().getInputCache().containsRecipeCrop(getHandlerWorld(), stack);
    }

    default boolean containsRecipeSoil(ItemStack stack) {
        return getRecipeType().getInputCache().containsRecipeSoil(getHandlerWorld(), stack);
    }

    default boolean containsRecipeFluid(FluidStack stack) {
        return getRecipeType().getInputCache().containsRecipeFluid(getHandlerWorld(), stack);
    }

    default boolean containsRecipeCropOther(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsCropOther(getHandlerWorld(), cropStack, soilStack, fluidStack);
    }

    default boolean containsRecipeSoilOther(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsSoilOther(getHandlerWorld(), cropStack, soilStack, fluidStack);
    }

    default boolean containsRecipeFluidOther(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsFluidOther(getHandlerWorld(), cropStack, soilStack, fluidStack);
    }

    default CropSoilRecipe findFirstRecipe(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), cropStack, soilStack, fluidStack);
    }

    default CropSoilRecipe findFirstRecipe(IInputHandler<ItemStack> cropHandler, IInputHandler<ItemStack> soilHandler,
            IInputHandler<FluidStack> fluidHandler) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), cropHandler.getInput(),
                soilHandler.getInput(), fluidHandler.getInput());
    }

    public IExtendedFluidTank getFluidTank();

    public MachineEnergyContainer<BE> getEnergyContainer();

    public double getScaledProgress();

}