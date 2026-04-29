package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.base.BENetworkConfigurableMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.FluidIngredientCardItem;
import mekanism.api.IContentsListener;
import mekanism.api.heat.HeatAPI;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAppliedTEP extends BENetworkConfigurableMachine implements IAppliedSingleToSingleMachine {

    private BasicInventorySlot cardSlot;
    private AEFluidKey inputKey;
    private AEFluidKey outputKey;
    private long inputAmount;
    private long outputAmount;
    private BasicHeatCapacitor heatCapacitor;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;

    public BEAppliedTEP(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.HEAT);
        configComponent.setupOutputConfig(TransmissionType.HEAT, heatCapacitor);
        ejectorComponent = new TileComponentEjector(this);
        lastEnvironmentLoss = 0d;
        lastTransferLoss = 0d;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(
                cardSlot = InputInventorySlot.at(stack -> stack.getItem() instanceof FluidIngredientCardItem, () -> {
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
        if (is.getItem() instanceof FluidIngredientCardItem cardItem) {
            AEFluidKey key = cardItem.getKey(is);
            if (key != null && hasLevel()) {
                FluidStack keyStack = key.toStack(0x7fffffff);
                level.getRecipeManager().getAllRecipesFor(MekanismRecipeType.EVAPORATING.get())
                        .stream()
                        .filter(r -> r.test(keyStack))
                        .findFirst()
                        .ifPresentOrElse(r -> {
                            inputKey = key;
                            FluidStack output = r.getOutput(keyStack);
                            outputKey = AEFluidKey.of(output);
                            inputAmount = r.getInput().getNeededAmount(keyStack);
                            outputAmount = output.getAmount();
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener,
            CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(
                MekanismConfig.general.evaporationHeatCapacity.get() * 18,
                ambientTemperature, listener));
        return builder.build();
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
            operations = Math.min(operations, (long) ((heatCapacitor.getTemperature() - HeatAPI.AMBIENT_TEMP)
                    * MekanismConfig.general.evaporationTempMultiplier.get()));
            if (operations > 0) {
                storage.extract(inputKey, operations * inputAmount, Actionable.MODULATE, source);
                storage.insert(outputKey, operations * outputAmount, Actionable.MODULATE, source);
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

    public BasicHeatCapacitor getHeatCapacitor() {
        return heatCapacitor;
    }

    public double getLastTransferLoss() {
        return lastTransferLoss;
    }

    public double getLastEnvironmentLoss() {
        return lastEnvironmentLoss;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastTransferLoss, value -> lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, value -> lastEnvironmentLoss = value));
    }
}
