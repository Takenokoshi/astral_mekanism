package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.ChemicalIngredientCardItem;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.InfusionStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.PigmentStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.SlurryStackIngredient;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedCrystallizer extends BEAppliedEnergizedMachine implements IAppliedSingleToSingleMachine {

    private BasicInventorySlot cardSlot;
    private AEKey inputKey;
    private AEKey outputKey;
    private long inputAmount;
    private long outputAmount;

    public BEAppliedCrystallizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.chemicalCrystallizer.get()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> stack.getItem() instanceof ChemicalIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKey = null;
        outputKey = null;
        inputAmount = 0;
        outputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlot.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack is = cardSlot.getStack();
        if (is.getItem() instanceof ChemicalIngredientCardItem cardItem) {
            MekanismKey key = cardItem.getKey(is);
            if (key != null && hasLevel()) {
                ChemicalStack<?> stack = key.withAmount(Long.MAX_VALUE);
                level.getRecipeManager().getAllRecipesFor(MekanismRecipeType.CRYSTALLIZING.get())
                        .stream().filter(r -> r.test(stack)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKey = key;
                            ItemStack output = r.getOutput(BoxedChemicalStack.box(stack));
                            ChemicalStackIngredient<?, ?> ingredient = r.getInput();
                            if (stack instanceof GasStack gasStack
                                    && ingredient instanceof GasStackIngredient gasIngredient) {
                                inputAmount = gasIngredient.getNeededAmount(gasStack);
                            } else if (stack instanceof InfusionStack gasStack
                                    && ingredient instanceof InfusionStackIngredient gasIngredient) {
                                inputAmount = gasIngredient.getNeededAmount(gasStack);
                            } else if (stack instanceof PigmentStack gasStack
                                    && ingredient instanceof PigmentStackIngredient gasIngredient) {
                                inputAmount = gasIngredient.getNeededAmount(gasStack);
                            } else if (stack instanceof SlurryStack gasStack
                                    && ingredient instanceof SlurryStackIngredient gasIngredient) {
                                inputAmount = gasIngredient.getNeededAmount(gasStack);
                            } else {
                                inputAmount = 0;
                            }
                            outputKey = AEItemKey.of(output);
                            outputAmount = output.getCount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        MEStorage storage = getMeStorage();
        if (MekanismUtils.canFunction(this) && inputAmount > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    storage.extract(inputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount,
                    storage.insert(outputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / outputAmount);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                storage.insert(outputKey, operations * outputAmount, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey() {
        return inputKey;
    }

    public AEKey getOutputKey() {
        return outputKey;
    }

}
