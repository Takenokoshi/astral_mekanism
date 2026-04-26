package astral_mekanism.item.recipecard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEFluidKey;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.AEBaseItem;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.gas.attribute.GasAttributes.CooledCoolant;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class CoolantCardItem extends AEBaseItem implements ICellWorkbenchItem {

    public CoolantCardItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(key -> {
            if (key instanceof AEFluidKey fluidKey && fluidKey.getFluid().equals(Fluids.WATER)) {
                return true;
            }
            if (key instanceof MekanismKey mekanismKey && mekanismKey.getForm() == MekanismKey.GAS) {
                return mekanismKey.getStack().has(CooledCoolant.class);
            }
            return false;
        }, is, 1);
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
