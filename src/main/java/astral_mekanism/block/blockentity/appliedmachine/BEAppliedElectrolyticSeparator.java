package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ElectrolysisRecipe.ElectrolysisRecipeOutput;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAppliedElectrolyticSeparator extends BEAppliedEnergizedMachine {
    private BasicInventorySlot cardSlot;
    private AEFluidKey inputKey;
    private MekanismKey firstOutputKey;
    private MekanismKey secondOutputKey;
    private long inputAmount;
    private long firstOutputAmount;
    private long secondOutputAmount;

    public BEAppliedElectrolyticSeparator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.general.FROM_H2.get().multiply(2)
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> {
                    Item item = stack.getItem();
                    return item instanceof FluidIngredientCardItem || item instanceof FluidIngredientCardItem;
                }, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKey = null;
        firstOutputKey = null;
        secondOutputKey = null;
        inputAmount = 0;
        firstOutputAmount = 0;
        secondOutputAmount = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlot.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack is = cardSlot.getStack();
        if (is.getItem() instanceof FluidIngredientCardItem fluidCard) {
            AEFluidKey key = fluidCard.getKey(is);
            if (key != null && hasLevel()) {
                FluidStack fluidStack = key.toStack(0x7fffffff);
                level.getRecipeManager().getAllRecipesFor(MekanismRecipeType.SEPARATING.get())
                        .stream().filter(r -> r.test(fluidStack)).findFirst()
                        .ifPresentOrElse(r -> {
                            inputKey = key;
                            inputAmount = r.getInput().getNeededAmount(fluidStack);
                            ElectrolysisRecipeOutput output = r.getOutput(fluidStack);
                            firstOutputKey = MekanismKey.of(output.left());
                            secondOutputKey = MekanismKey.of(output.right());
                            firstOutputAmount = output.left().getAmount();
                            secondOutputAmount = output.right().getAmount();
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
        if (MekanismUtils.canFunction(this) && inputKey != null && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = Math.min(
                    storage.insert(firstOutputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / firstOutputAmount,
                    storage.insert(secondOutputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / secondOutputAmount);
            operations = Math.min(operations,
                    storage.extract(inputKey, Long.MAX_VALUE, Actionable.SIMULATE, source) / inputAmount);
            operations = Math.min(operations, getSupportableOperations(storage, source));
            if (operations > 0) {
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                consumeEnergy(storage, source, operations);
                storage.insert(firstOutputKey, operations * firstOutputAmount, Actionable.MODULATE, source);
                storage.insert(secondOutputKey, operations * secondOutputAmount, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    public AEKey getInputKey() {
        return inputKey;
    }

    public AEKey getFirstOutputKey() {
        return firstOutputKey;
    }

    public AEKey getSecondOutputKey() {
        return secondOutputKey;
    }

}
