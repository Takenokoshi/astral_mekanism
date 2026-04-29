package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import astral_mekanism.item.recipecard.GasIngredientCardItem;
import astral_mekanism.util.AMEKeyUtils;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.providers.IBlockProvider;
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

public class BEAppliedRotaryCondensentrator extends BEAppliedEnergizedMachine implements IAppliedSingleToSingleMachine {

    private BasicInventorySlot cardSlot;
    private AEKey inputKey;
    private AEKey outputKey;
    private long inputAmount;
    private long outputAmount;

    public BEAppliedRotaryCondensentrator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.rotaryCondensentrator.get()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> {
                    Item item = stack.getItem();
                    return item instanceof FluidIngredientCardItem || item instanceof GasIngredientCardItem;
                }, () -> {
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
        if (is.getItem() instanceof GasIngredientCardItem gasCard) {
            MekanismKey key = gasCard.getKey(is);
            if (key != null && hasLevel()) {
                GasStack gasStack = AMEKeyUtils.getGas(key);
                gasStack.setAmount(Long.MAX_VALUE);
                level.getRecipeManager().getAllRecipesFor(MekanismRecipeType.ROTARY.get())
                        .stream().filter(r -> r.test(gasStack)).findFirst().ifPresentOrElse(recipe -> {
                            inputKey = key;
                            FluidStack fluidStack = recipe.getFluidOutput(gasStack);
                            outputKey = AEFluidKey.of(fluidStack);
                            inputAmount = recipe.getGasInput().getNeededAmount(gasStack);
                            outputAmount = fluidStack.getAmount();
                        }, this::removeRecipeInfo);
                return;
            }
        } else if (is.getItem() instanceof FluidIngredientCardItem fluidCard) {
            AEFluidKey key = fluidCard.getKey(is);
            if (key != null && hasLevel()) {
                FluidStack fluidStack = key.toStack(0x7fffffff);
                level.getRecipeManager().getAllRecipesFor(MekanismRecipeType.ROTARY.get())
                        .stream().filter(r -> r.test(fluidStack)).findFirst().ifPresentOrElse(
                                recipe -> {
                                    inputKey = key;
                                    GasStack gasStack = recipe.getGasOutput(fluidStack);
                                    outputKey = MekanismKey.of(gasStack);
                                    inputAmount = recipe.getFluidInput().getNeededAmount(fluidStack);
                                    outputAmount = gasStack.getAmount();
                                }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
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
