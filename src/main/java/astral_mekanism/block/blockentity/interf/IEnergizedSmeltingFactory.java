package astral_mekanism.block.blockentity.interf;

import java.util.List;
import java.util.Set;

import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IGeneralSingelRecipeLookupHandler;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public interface IEnergizedSmeltingFactory<BE extends TileEntityMekanism & IEnergizedSmeltingFactory<BE> & IEnergizedMachine<BE>>
        extends
        IGeneralSingelRecipeLookupHandler<ItemStack, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>>,
        IEnergizedMachine<BE> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            IEssentialEnergizedSmelter.NOT_ENOUGH_ITEM_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    public static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY,
            IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE);

    public IInfusionTank getInfusionTank();

    public default void receive(ServerPlayer player) {
        IInfusionTank infusionTank = getInfusionTank();
        if (!infusionTank.isEmpty() && infusionTank.getType() == AstralMekanismInfuseTypes.XP.get()) {
            int give = (int) (Math.min(infusionTank.getStored() / 100, 0x7fffffff));
            player.giveExperiencePoints(give);
            infusionTank.shrinkStack(100l * give, Action.EXECUTE);
        }
    }

    public abstract double getProgressScaled(int index);
}
