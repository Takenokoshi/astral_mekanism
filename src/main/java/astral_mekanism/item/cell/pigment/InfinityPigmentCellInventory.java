package astral_mekanism.item.cell.pigment;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.MekanismAPI;
import net.minecraft.network.chat.Component;

public class InfinityPigmentCellInventory implements StorageCell {

    private static final long amount = 1l << 42;

    @Override
    public Component getDescription() {
        return Component.literal("Infinity Pigment!!");
    }

    @Override
    public double getIdleDrain() {
        return 0.01d;
    }

    @Override
    public CellState getStatus() {
        return CellState.FULL;
    }

    @Override
    public void persist() {
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        MekanismAPI.pigmentRegistry().forEach(pigment -> {
            if (!pigment.isEmptyType()) {
                out.add(MekanismKey.of(pigment.getStack(0x7fffffffffffffffl)), amount);
            }
        });
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (what instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.PIGMENT) {
            return amount;
        }
        return 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (what instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.PIGMENT) {
            return amount;
        }
        return 0;
    }

}
