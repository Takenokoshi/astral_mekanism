package astral_mekanism.block.blockentity.interf;

import java.util.List;

import astral_mekanism.block.blockentity.prefab.BlockEntityRecipeMachine;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IGeneralSingelRecipeLookupHandler;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public interface IEssentialEnergizedSmelter<BE extends BlockEntityRecipeMachine<SmeltingRecipe> & IEssentialEnergizedSmelter<BE>> extends
        IGeneralSingelRecipeLookupHandler<ItemStack, SmeltingRecipe, GeneralSingleItem<Container, SmeltingRecipe>> {

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

    public default void receive(ServerPlayer player){
        IInfusionTank infusionTank = getInfusionTank();
        if (!infusionTank.isEmpty() && infusionTank.getStack().getRaw() == AstralMekanismInfuseTypes.XP.get()) {
            int give = (int) (Math.min(infusionTank.getStored() / 100, 0x7fffffff));
            player.giveExperiencePoints(give);
            infusionTank.shrinkStack(give * 100, Action.EXECUTE);
        }
    };
}
