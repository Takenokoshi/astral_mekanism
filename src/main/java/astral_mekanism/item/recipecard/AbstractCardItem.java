package astral_mekanism.item.recipecard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.AEBaseItem;
import appeng.items.storage.StorageCellTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public abstract class AbstractCardItem extends AEBaseItem implements ICellWorkbenchItem {

    public AbstractCardItem(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        var inv = getConfigInventory(is);
        if (inv == null || inv.isEmpty()) {
            return Optional.empty();
        }
        return Optional
                .of(new StorageCellTooltipComponent(Collections.emptyList(), List.of(inv.getStack(0)), false, false));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, @NotNull List<Component> lines,
            @NotNull TooltipFlag adv) {
        lines.add(Component.literal("Set Information on Cell Workbench."));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is) {
        return UpgradeInventories.empty();
    }

}
