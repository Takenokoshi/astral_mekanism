package astral_mekanism.block.blockentity.interf;

import java.util.List;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.TileEntityChemicalTank.GasMode;
import mekanism.common.tile.interfaces.IHasGasMode;
import mekanism.common.tile.interfaces.ISustainedData;
import net.minecraft.server.level.ServerPlayer;
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

    public default void receive(ServerPlayer player) {
        IInfusionTank infusionTank = getInfusionTank();
        if (!infusionTank.isEmpty() && infusionTank.getType() == AstralMekanismInfuseTypes.XP.get()) {
            giveXp(player, infusionTank.getStored() / 100);
            infusionTank.setStackSize(infusionTank.getStored() % 100, Action.EXECUTE);
        }
    };

    public static void giveXp(ServerPlayer player, long amount) {
        int nowLevel = player.experienceLevel;
        amount += nowLevel < 17 ? nowLevel * (nowLevel + 6)
                : nowLevel < 32 ? 2.5f * nowLevel * nowLevel - 40.5f * nowLevel + 360
                        : 4.5f * nowLevel * nowLevel - 162.5 * nowLevel + 2220;
        int toLevel = (int) (amount < 353 ? Math.sqrt(amount + 9) - 3
                : amount < 1058 ? Math.sqrt(0.4f * (amount - 7839f / 40)) + 8.1f
                        : Math.sqrt(2f / 9 * (amount - 54215f / 72)));
        player.setExperienceLevels(toLevel);
    }

    public default void handleTank() {
        IInfusionTank infusionTank = getInfusionTank();
        GasMode gasMode = getGasMode();
        if (gasMode == GasMode.DUMPING) {
            if (infusionTank.getStored() > infusionTank.getCapacity() / 5 * 4) {
                infusionTank.shrinkStack(infusionTank.getCapacity() / 5 * 4, Action.EXECUTE);
            }
            infusionTank.shrinkStack(infusionTank.getCapacity() / 100, Action.EXECUTE);
        } else if (gasMode == GasMode.DUMPING_EXCESS) {
            if (infusionTank.getStored() > infusionTank.getCapacity() / 5 * 4) {
                infusionTank.shrinkStack(infusionTank.getCapacity() / 5 * 4, Action.EXECUTE);
            }
        }
    }

    public abstract GasMode getGasMode();
}
