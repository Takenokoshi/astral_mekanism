package astral_mekanism.block.blockentity.interf;

import java.util.List;

import appeng.recipes.handlers.InscriberRecipe;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.lookup.cache.recipe.InscriberRecipeInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeTypedLookupHandler;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.item.ItemStack;

public interface IMekanicalInscriber<BE extends TileEntityMekanism & IMekanicalInscriber<BE>>
        extends IUnifiedRecipeTypedLookupHandler<InscriberRecipe, InscriberRecipeInputRecipeCache>,
        IEnergizedMachine<BE> {

    public static final RecipeError NOT_ENOUGH_TOP_INPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_MIDDLE_INPUT = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_BOTTOM_INPUT = RecipeError.create();
    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            NOT_ENOUGH_TOP_INPUT,
            NOT_ENOUGH_MIDDLE_INPUT,
            NOT_ENOUGH_BOTTOM_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    default boolean containsInputT(ItemStack input) {
        return getRecipeType().getInputCache().containsInputTop(getHandlerWorld(), input);
    }

    default boolean containsInputM(ItemStack input) {
        return getRecipeType().getInputCache().containsInputMiddle(getHandlerWorld(), input);
    }

    default boolean containsInputB(ItemStack input) {
        return getRecipeType().getInputCache().containsInputBottom(getHandlerWorld(), input);
    }

    default boolean containsInputTMB(ItemStack inputT, ItemStack inputM, ItemStack inputB) {
        return getRecipeType().getInputCache().containsTMB(getHandlerWorld(), inputT, inputM, inputB);
    }

    default boolean containsInputMTB(ItemStack inputT, ItemStack inputM, ItemStack inputB) {
        return getRecipeType().getInputCache().containsMTB(getHandlerWorld(), inputT, inputM, inputB);
    }

    default boolean containsInputBTM(ItemStack inputT, ItemStack inputM, ItemStack inputB) {
        return getRecipeType().getInputCache().containsBTM(getHandlerWorld(), inputT, inputM, inputB);
    }

    default InscriberRecipe findFirstRecipe(ItemStack inputT, ItemStack inputM, ItemStack inputB) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), inputT, inputM, inputB);
    }

    default InscriberRecipe findFirstRecipe(IInputHandler<ItemStack> handlerT, IInputHandler<ItemStack> handlerM,
            IInputHandler<ItemStack> handlerB) {
        return this.findFirstRecipe(handlerT.getInput(), handlerM.getInput(), handlerB.getInput());
    }

    @Override
    default IUnifiedRecipeTypeProvider<InscriberRecipe, InscriberRecipeInputRecipeCache> getRecipeType() {
        return GeneralRecipeType.INSCRIBE;
    }
}
