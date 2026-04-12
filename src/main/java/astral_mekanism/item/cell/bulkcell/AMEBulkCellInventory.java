package astral_mekanism.item.cell.bulkcell;

import java.math.BigInteger;
import java.util.function.Function;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AMEBulkCellInventory<AEKEY extends AEKey> implements StorageCell {

    private static final String KEY = "key";
    private static final String UNIT_COUNT = "smallestUnitCount";

    private static final long STACK_LIMIT = 1l << 42;

    private final ISaveProvider container;
    private final ItemStack stack;
    private final Function<AEKey, AEKEY> caster;

    private AEKEY sortedKey;
    private final AEKEY filerKey;
    private BigInteger unitCount;
    private boolean isPersisted = true;

    public AMEBulkCellInventory(ItemStack stack, ISaveProvider container, Function<AEKey, AEKEY> caster,
            Function<CompoundTag, AEKEY> reader) {
        this.container = container;
        this.stack = stack;
        this.caster = caster;
        AMEBulkCellItem<?> item = (AMEBulkCellItem<?>) stack.getItem();
        this.filerKey = caster.apply(item.getConfigInventory(stack).getKey(0));
        this.sortedKey = getTag().contains(KEY) ? reader.apply(getTag().getCompound(KEY)) : null;
        unitCount = !getTag().getString(UNIT_COUNT).isEmpty()
                ? new BigInteger(getTag().getString(UNIT_COUNT))
                : BigInteger.ZERO;

    }

    private CompoundTag getTag() {
        return stack.getOrCreateTag();
    }

    private long clampedLong(BigInteger toClamp, long limit) {
        return toClamp.min(BigInteger.valueOf(limit)).longValue();
    }

    @Override
    public CellState getStatus() {
        if (sortedKey == null || unitCount.signum() < 1) {
            return CellState.EMPTY;
        }

        if (!sortedKey.equals(filerKey)) {
            return CellState.FULL;
        }

        return CellState.NOT_EMPTY;
    }

    public AEKEY getSortedKey() {
        return sortedKey;
    }

    public long getStoredQuantity() {
        return clampedLong(unitCount, Long.MAX_VALUE);
    }

    public AEKEY getFilterKey() {
        return filerKey;
    }

    @Override
    public double getIdleDrain() {
        return 5.0f;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        AEKEY key = caster.apply(what);
        if (amount == 0 || key == null) {
            return 0;
        }
        if (filerKey == null) {
            return 0;
        }
        if (!what.equals(filerKey)) {
            return 0;
        }
        if (sortedKey != null && !filerKey.equals(sortedKey)) {
            return 0;
        }
        var units = BigInteger.valueOf(amount);
        if (mode == Actionable.MODULATE) {
            if (sortedKey == null) {
                sortedKey = filerKey;
            }

            unitCount = unitCount.add(units);
            saveChanges();
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        AEKEY key = caster.apply(what);
        if (amount == 0 || key == null) {
            return 0;
        }
        if (filerKey == null) {
            return 0;
        }
        if (!what.equals(filerKey)) {
            return 0;
        }
        if (sortedKey != null && !filerKey.equals(sortedKey)) {
            return 0;
        }
        var units = BigInteger.valueOf(amount);
        var currentUnitCount = unitCount;
        if (currentUnitCount.compareTo(units) <= 0) {
            if (mode == Actionable.MODULATE) {
                sortedKey = null;
                unitCount = BigInteger.ZERO;
                saveChanges();
            }
            return clampedLong(currentUnitCount, amount);
        } else {
            if (mode == Actionable.MODULATE) {
                unitCount = unitCount.subtract(units);
                saveChanges();
            }
            return amount;
        }
    }

    private void saveChanges() {
        isPersisted = false;
        if (container != null) {
            container.saveChanges();
        } else {
            persist();
        }
    }

    @Override
    public void persist() {
        if (isPersisted) {
            return;
        }
        if (sortedKey == null || unitCount.signum() < 1) {
            getTag().remove(KEY);
            getTag().remove(UNIT_COUNT);
        } else {
            getTag().put(KEY, sortedKey.toTagGeneric());
            getTag().putString(UNIT_COUNT, unitCount.toString());
        }
        isPersisted = true;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        if (sortedKey != null) {
            out.add(sortedKey, clampedLong(unitCount, STACK_LIMIT));
        }
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        AEKEY key = caster.apply(what);
        return key != null
                ? key.equals(sortedKey) || key.equals(filerKey)
                : false;
    }

    @Override
    public boolean canFitInsideCell() {
        return filerKey == null && sortedKey == null && unitCount.signum() < 1;
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }

}
