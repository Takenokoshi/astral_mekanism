package astral_mekanism.block.blockentity.generator;

import org.jetbrains.annotations.NotNull;

import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.config.AFConfig;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import appeng.util.ConfigInventory;
import astral_mekanism.block.blockentity.base.BENetworkMekanismMachine;
import astral_mekanism.block.blockentity.interf.IPacketReceiverSetLong;
import astral_mekanism.item.recipecard.FuelCardItem;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes.Fuel;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedGasBurningGenerator extends BENetworkMekanismMachine implements IPacketReceiverSetLong {

    private BasicInventorySlot fuelCardSlot;
    private long efficiency;
    public final FluxKey energyKey;
    private MekanismKey fuelKey;
    private long fePerFuel;
    private long efficiencyCap;
    private final int fePerByte;

    public BEAppliedGasBurningGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        energyKey = FluxKey.of(EnergyType.FE);
        fePerByte = AFConfig.getFluxPerByte();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(fuelCardSlot = BasicInventorySlot.at(stack -> stack.getItem() instanceof FuelCardItem, () -> {
            listener.onContentsChanged();
            recalculateFuelData();
        }, 64, 53));
        return builder.build();
    }

    private void recalculateFuelData() {
        if (fuelCardSlot.isEmpty()) {
            clearFuelData();
            return;
        }
        ItemStack is = fuelCardSlot.getStack();
        if (is.getItem() instanceof FuelCardItem cardItem) {
            ConfigInventory inv = cardItem.getConfigInventory(is);
            if (!inv.isEmpty()
                    && inv.getKey(0) instanceof MekanismKey mekanismKey
                    && mekanismKey.getForm() == MekanismKey.GAS
                    && mekanismKey.getStack() instanceof GasStack gasStack
                    && gasStack.has(Fuel.class)) {
                fuelKey = mekanismKey;
                gasStack.ifAttributePresent(Fuel.class, fuelAttr -> {
                    fePerFuel = fuelAttr.getEnergyPerTick().multiply(fuelAttr.getBurnTicks())
                            .divideToLong(MekanismConfig.general.forgeConversionRate.get());
                });
                if (fePerFuel < 1) {
                    clearFuelData();
                    return;
                }
                efficiencyCap = Long.MAX_VALUE / fePerFuel;
                return;
            }

        }
        clearFuelData();
        return;
    }

    private void clearFuelData() {
        fuelKey = null;
        fePerFuel = 0;
        efficiencyCap = 0;
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        if (MekanismUtils.canFunction(this) && fePerFuel > 0 && efficiency > 0) {
            if (efficiency > efficiencyCap) {
                efficiency = efficiencyCap;
            }
            MEStorage storage = getMeStorage();
            if (storage == null) {
                setActive(false);
                return;
            }
            IActionSource source = IActionSource.ofMachine(this);
            long burnable = storage.extract(fuelKey, efficiency, Actionable.SIMULATE, source);
            burnable = storage.insert(energyKey, burnable * fePerFuel / fePerByte, Actionable.SIMULATE, source)
                    * fePerFuel;
            if (burnable > 0) {
                storage.extract(fuelKey, burnable, Actionable.MODULATE, source);
                storage.insert(energyKey, burnable * fePerFuel / fePerByte, Actionable.MODULATE, source);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

    @Override
    public void receive(int num, long value) {
        efficiency = value;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        NBTUtils.setLongIfPresent(nbt, "efficiency", value -> efficiency = value);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putLong("efficiency", efficiency);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEfficiency, v -> efficiency = v));
    }

    public long getEfficiency() {
        return efficiency;
    }

    public MekanismKey getFuelKey() {
        return fuelKey;
    }
}
