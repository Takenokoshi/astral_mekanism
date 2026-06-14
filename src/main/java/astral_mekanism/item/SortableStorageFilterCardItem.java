package astral_mekanism.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import astral_mekanism.AMELang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SortableStorageFilterCardItem extends Item implements ICellWorkbenchItem {

    public SortableStorageFilterCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEItemKey::is, is);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is) {
        return UpgradeInventories.empty();
    }

    public List<ItemStack> getFilterItems(ItemStack is) {
        ConfigInventory inv = getConfigInventory(is);
        List<ItemStack> result = new ArrayList<>();
        inv.keySet().forEach(key -> {
            if (key instanceof AEItemKey itemKey) {
                result.add(itemKey.getReadOnlyStack());
            }
        });
        return Collections.unmodifiableList(result);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack arg0) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack arg0, FuzzyMode arg1) {
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, @NotNull List<Component> lines,
            @NotNull TooltipFlag adv) {
        lines.add(AMELang.DESCRIPTION_SORTABLE_STORAGE_FILTER_CARD.translate());
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        List<GenericStack> content = new ArrayList<>(getConfigInventory(is).keySet().stream()
                .map(key -> new GenericStack(key, 1)).toList());
        boolean hasMore = content.size() > 9;
        if (hasMore) {
            content.subList(9, content.size()).clear();
        }
        return Optional.of(new StorageCellTooltipComponent(List.of(), content, hasMore, false));
    }

}
