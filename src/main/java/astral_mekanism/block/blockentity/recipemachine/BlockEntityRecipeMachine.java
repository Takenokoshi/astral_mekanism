package astral_mekanism.block.blockentity.recipemachine;

import java.util.List;
import java.util.function.BooleanSupplier;

import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityRecipeMachine<RECIPE extends Recipe<?>> extends TileEntityConfigurableMachine implements IGeneralRecipeLookUpHandler<RECIPE> {

    protected final BooleanSupplier recheckAllRecipeErrors;
    private final List<RecipeError> errorTypes;
    private final boolean[] trackedErrors;

    protected BlockEntityRecipeMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state,
            List<RecipeError> errorTypes) {
        super(blockProvider, pos, state);
        this.recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(this);
        this.errorTypes = List.copyOf(errorTypes);
        trackedErrors = new boolean[this.errorTypes.size()];
    }

}
