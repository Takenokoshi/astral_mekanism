package astral_mekanism.recipes.lookup;

import org.checkerframework.checker.nullness.qual.Nullable;

import astral_mekanism.recipes.inputRecipeCache.MekanicalTransformRecipeCache;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler.IRecipeTypedLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface MekanicalTransformRecipeLookUpHandler
        extends IRecipeTypedLookupHandler<MekanicalTransformRecipe, MekanicalTransformRecipeCache> {

    default boolean containsRecipeIAOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        return getRecipeType().getInputCache().containsInputIAOther(getHandlerWorld(), inputIA, inputIB, inputIC,
                inputFA, inputFB);
    }

    default boolean containsRecipeIBOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        return getRecipeType().getInputCache().containsInputIBOther(getHandlerWorld(), inputIA, inputIB, inputIC,
                inputFA, inputFB);
    }

    default boolean containsRecipeICOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        return getRecipeType().getInputCache().containsInputICOther(getHandlerWorld(), inputIA, inputIB, inputIC,
                inputFA, inputFB);
    }

    default boolean containsRecipeFAOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        return getRecipeType().getInputCache().containsInputFAOther(getHandlerWorld(), inputIA, inputIB, inputIC,
                inputFA, inputFB);
    }

    default boolean containsRecipeFBOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC, FluidStack inputFA,
            FluidStack inputFB) {
        return getRecipeType().getInputCache().containsInputFBOther(getHandlerWorld(), inputIA, inputIB, inputIC,
                inputFA, inputFB);
    }

    default boolean containsRecipeIA(ItemStack input) {
        return getRecipeType().getInputCache().containsInputIA(getHandlerWorld(), input);
    }

    default boolean containsRecipeIB(ItemStack input) {
        return getRecipeType().getInputCache().containsInputIB(getHandlerWorld(), input);
    }

    default boolean containsRecipeIC(ItemStack input) {
        return getRecipeType().getInputCache().containsInputIC(getHandlerWorld(), input);
    }

    default boolean containsRecipeFA(FluidStack input) {
        return getRecipeType().getInputCache().containsInputFA(getHandlerWorld(), input);
    }

    default boolean containsRecipeFB(FluidStack input) {
        return getRecipeType().getInputCache().containsInputFB(getHandlerWorld(), input);
    }

    default @Nullable MekanicalTransformRecipe findFirstRecipe(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
            FluidStack inputFA, FluidStack inputFB) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), inputIA, inputIB, inputIC, inputFA,
                inputFB);
    }

    default @Nullable MekanicalTransformRecipe findFirstRecipe(IInputHandler<ItemStack> inputHandlerIA,
            IInputHandler<ItemStack> inputHandlerIB, IInputHandler<ItemStack> inputHandlerIC,
            IInputHandler<FluidStack> inputHandlerFA, IInputHandler<FluidStack> inputHandlerFB) {
        return findFirstRecipe(inputHandlerIA.getInput(), inputHandlerIB.getInput(), inputHandlerIC.getInput(),
                inputHandlerFA.getInput(), inputHandlerFB.getInput());
    }
}
