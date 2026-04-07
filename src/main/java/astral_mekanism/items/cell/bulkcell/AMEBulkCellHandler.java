package astral_mekanism.items.cell.bulkcell;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import me.ramidzkh.mekae2.ae2.MekanismKeyType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AMEBulkCellHandler<AEKEY extends AEKey> implements ICellHandler {

    private final Function<AEKey, AEKEY> caster;
    private final Function<CompoundTag, AEKEY> reader;
    private final Class<AEKEY> clazz;

    public AMEBulkCellHandler(Function<AEKey, AEKEY> caster, Function<CompoundTag, AEKEY> reader, Class<AEKEY> clazz) {
        this.caster = caster;
        this.reader = reader;
        this.clazz = clazz;
    }

    @Override
    public @Nullable AMEBulkCellInventory<AEKEY> getCellInventory(ItemStack stack, @Nullable ISaveProvider provider) {
        return isCell(stack) ? new AMEBulkCellInventory<>(stack, provider, caster, reader) : null;
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return stack != null ? stack.getItem() instanceof AMEBulkCellItem<?> cellItem && cellItem.classCheck(clazz) : false;
    }

    public static final AMEBulkCellHandler<AEFluidKey> FLUID_HANDLER = new AMEBulkCellHandler<>(
            key -> key instanceof AEFluidKey fluidKey ? fluidKey : null,
            AEFluidKey::fromTag,
            AEFluidKey.class);

    public static final AMEBulkCellHandler<MekanismKey> CHEMICAL_HANDLER = new AMEBulkCellHandler<>(
            key -> key instanceof MekanismKey mekKey ? mekKey : null,
            t -> (MekanismKey) (MekanismKeyType.TYPE.loadKeyFromTag(t)),
            MekanismKey.class);
}
