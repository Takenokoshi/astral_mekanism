package astral_mekanism.generalrecipe.cachedrecipe;

import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

import astral_mekanism.block.blockentity.core.BlockEntityUtils;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CropSoilCachedRecipe extends GeneralCachedRecipe<CropSoilRecipe> {

    private final IInputHandler<ItemStack> cropHandler;
    private final IInputHandler<ItemStack> soilHandler;
    private final IInputHandler<FluidStack> waterHandler;
    private final HarvestEntriesOutputHandler outputHandler;

    private ItemStack cropStack = ItemStack.EMPTY;
    private ItemStack soilStack = ItemStack.EMPTY;
    private FluidStack waterStack = FluidStack.EMPTY;
    private List<HarvestEntry> harvestEntries = List.of();

    public CropSoilCachedRecipe(CropSoilRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> cropHandler, IInputHandler<ItemStack> soilHandler,
            HarvestEntriesOutputHandler outputHandler, IInputHandler<FluidStack> waterHandler) {
        super(recipe, recheckAllErrors);
        this.cropHandler = cropHandler;
        this.soilHandler = soilHandler;
        this.waterHandler = waterHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            cropStack = cropHandler.getRecipeInput(recipe.getCrop());
            soilStack = soilHandler.getRecipeInput(recipe.getSoil());
            waterStack = waterHandler.getRecipeInput(recipe.getWater());
            harvestEntries = recipe.getOutput();
            if (cropStack.isEmpty() || soilStack.isEmpty() || waterStack.isEmpty() || harvestEntries.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            cropHandler.calculateOperationsCanSupport(tracker, cropStack);
            soilHandler.calculateOperationsCanSupport(tracker, soilStack);
            waterHandler.calculateOperationsCanSupport(tracker, waterStack);
            outputHandler.calculateOperationsCanSupport(tracker, harvestEntries);
        }
    }

    @Override
    protected void finishProcessing(int operations) {
        if (cropStack.isEmpty() || soilStack.isEmpty() || waterStack.isEmpty() || harvestEntries.isEmpty()) {
            return;
        }
        waterHandler.use(waterStack, operations);
        outputHandler.handleOutput(harvestEntries, operations);
    }

    @Override
    public boolean isInputValid() {
        boolean result = cropHandler.getInput().isEmpty() ? false : recipe.getCrop().test(cropHandler.getInput());
        result &= soilHandler.getInput().isEmpty() ? false : recipe.getSoil().test(soilHandler.getInput());
        result &= waterHandler.getInput().isEmpty()?false:recipe.getWater().test(waterHandler.getInput());
        return result;
    }

    public static class HarvestEntriesOutputHandler implements IOutputHandler<List<HarvestEntry>> {

        private final RecipeError notEnoughSpaceError;
        private final IInventorySlot[] slots;
        private final Random rng;

        public HarvestEntriesOutputHandler(RecipeError notEnoughSpaceError, IInventorySlot... slots) {
            this.notEnoughSpaceError = notEnoughSpaceError;
            this.slots = slots;
            this.rng = new Random();
        }

        @Override
        public void handleOutput(List<HarvestEntry> toOutput, int operations) {
            for (HarvestEntry harvestEntry : toOutput) {
                int amount = Math.round(harvestEntry.getItem().getCount()
                        * harvestEntry.getChance()
                        * ((harvestEntry.getMaxRolls() + harvestEntry.getMinRolls()) / 2)
                        * operations
                        + rng.nextFloat(-0.5f, 0.5f));
                BlockEntityUtils.insertItem(slots, harvestEntry.getItem().copyWithCount(amount));
            }
        }

        @Override
        public void calculateOperationsCanSupport(OperationTracker tracker, List<HarvestEntry> toOutput) {
            int emptySlots = 0;
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].isEmpty()) {
                    emptySlots++;
                }
            }
            if (emptySlots < toOutput.size()) {
                tracker.addError(notEnoughSpaceError);
                tracker.mismatchedRecipe();
                return;
            }
            int p = 0x7fffffff;
            for (HarvestEntry harvestEntry : toOutput) {
                int q = 0x3fffffff
                        / harvestEntry.getMaxRolls()
                        / harvestEntry.getItem().getCount();
                if (p > q) {
                    p = q;
                }
            }
            if (p < 1) {
                tracker.addError(notEnoughSpaceError);
                tracker.mismatchedRecipe();
            } else {
                tracker.updateOperations(p);
            }
        }

    }

}
