package astral_mekanism.block.blockentity.interf;

import java.util.List;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public interface IEssentialEnergizedSmelter<BE extends BlockEntityRecipeMachine<SmeltingRecipe> & IEssentialEnergizedSmelter<BE>>
        extends
        IUnifiedSingelRecipeLookupHandler<ItemStack, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>>,
        IHasGasMode, ISustainedData {

    public static final RecipeError NOT_ENOUGH_ITEM_OUTPUT_SPACE = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INFUSE_OUTPUT_SPACE = RecipeError.create();

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            NOT_ENOUGH_ITEM_OUTPUT_SPACE,
            NOT_ENOUGH_INFUSE_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    public IInfusionTank getInfusionTank();

    public MachineEnergyContainer<BE> getEnergyContainer();

    public abstract double getProgressScaled();

    public abstract GasMode getGasMode();
}
