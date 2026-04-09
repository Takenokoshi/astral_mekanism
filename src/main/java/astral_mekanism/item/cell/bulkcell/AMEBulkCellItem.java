package astral_mekanism.item.cell.bulkcell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.AEKeyFilter;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.AEConfig;
import appeng.core.localization.Tooltips;
import appeng.items.AEBaseItem;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import gripe._90.megacells.definition.MEGATranslations;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class AMEBulkCellItem<AEKEY extends AEKey> extends AEBaseItem implements ICellWorkbenchItem {

    private final Class<AEKEY> clazz;
    private final AMEBulkCellHandler<AEKEY> handler;
    private final AEKeyFilter filter;

    public AMEBulkCellItem(Properties properties,
            Class<AEKEY> clazz,
            AMEBulkCellHandler<AEKEY> handler,
            AEKeyFilter filter) {
        super(properties.stacksTo(1));
        this.clazz = clazz;
        this.handler = handler;
        this.filter = filter;
    }

    public static AMEBulkCellItem<AEFluidKey> createFluid(Properties properties) {
        return new AMEBulkCellItem<>(properties,
                AEFluidKey.class,
                AMEBulkCellHandler.FLUID_HANDLER,
                AEFluidKey.filter());
    }

    public static AMEBulkCellItem<MekanismKey> createChemical(Properties properties) {
        return new AMEBulkCellItem<>(properties,
                MekanismKey.class,
                AMEBulkCellHandler.CHEMICAL_HANDLER,
                key -> key instanceof MekanismKey);
    }

    public <K extends AEKey> boolean classCheck(Class<K> clazz) {
        return this.clazz.equals(clazz);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(filter, is, 1);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is) {
        return UpgradeInventories.empty();
    }

    @Override
    public void appendHoverText(ItemStack is, Level level, @NotNull List<Component> lines, @NotNull TooltipFlag adv) {
        var inv = handler.getCellInventory(is, null);
        if (inv != null) {
            var storedKey = inv.getSortedKey();
            var filterKey = inv.getFilterKey();
            if (storedKey != null) {
                lines.add(Tooltips.of(MEGATranslations.Contains.text(storedKey.getDisplayName())));
                var quantity = inv.getStoredQuantity();
                lines.add(Tooltips.of(MEGATranslations.Quantity.text(
                        quantity < Long.MAX_VALUE
                                ? Tooltips.ofNumber(quantity)
                                : MEGATranslations.ALot.text().withStyle(Tooltips.NUMBER_TEXT))));
            } else {
                lines.add(Tooltips.of(MEGATranslations.Empty.text()));
            }
            if (filterKey != null) {
                if (storedKey == null) {
                    lines.add(Tooltips.of(MEGATranslations.PartitionedFor.text(filterKey.getDisplayName())));
                } else if (!storedKey.equals(filterKey)) {
                    lines.add(MEGATranslations.MismatchedFilter.text().withStyle(ChatFormatting.DARK_RED));
                }
            } else {
                lines.add(
                        storedKey != null
                                ? MEGATranslations.MismatchedFilter.text().withStyle(ChatFormatting.DARK_RED)
                                : Tooltips.of(MEGATranslations.NotPartitioned.text()));
            }
        }
    }

    @NotNull
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        var inv = handler.getCellInventory(is, null);
        if (inv == null) {
            return Optional.empty();
        }
        var content = new ArrayList<GenericStack>();
        if (AEConfig.instance().isTooltipShowCellContent()) {
            if (inv.getSortedKey() != null) {
                content.add(new GenericStack(inv.getSortedKey(), inv.getStoredQuantity()));
            } else if (inv.getFilterKey() != null) {
                content.add(new GenericStack(inv.getFilterKey(), 0));
            }
        }
        return Optional.of(new StorageCellTooltipComponent(Collections.emptyList(), content, false, true));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }

}
