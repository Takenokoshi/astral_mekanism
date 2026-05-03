package astral_mekanism.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.localization.GuiText;
import appeng.core.localization.Tooltips;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.util.WorldUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.pedroksl.ae2addonlib.api.IGridLinkedItem;

public class MekMachineUpgradeToolItem extends Item implements IGridLinkedItem, ICellWorkbenchItem {

    public MekMachineUpgradeToolItem(Properties props) {
        super(props.fireResistant().stacksTo(1));
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(EnumColor.BRIGHT_GREEN, super.getName(stack));
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Level world = context.getLevel();
            BlockEntity be = WorldUtils.getTileEntity(world, context.getClickedPos());
            if (be instanceof TileEntityMekanism tilemek) {
                addUpgrade(context.getItemInHand(), player, tilemek);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public void addUpgrade(ItemStack stack, Player player, TileEntityMekanism tile) {
        Level level = tile.getLevel();
        if (level == null || level.isClientSide || level != player.level()
                || !(stack.getItem() instanceof MekMachineUpgradeToolItem toolItem)) {
            return;
        }
        TileComponentUpgrade component = tile.getComponent();
        ConfigInventory config = toolItem.getConfigInventory(stack);
        IGrid grid = getLinkedGrid(stack, level);
        if (config.isEmpty() || grid == null || component == null) {
            return;
        }
        IStorageService storage = grid.getStorageService();
        if (storage == null) {
            return;
        }
        MEStorage inventory = storage.getInventory();
        if (inventory == null) {
            return;
        }
        IActionSource source = IActionSource.ofPlayer(player);
        for (int i = 0; i < config.size(); i++) {
            AEKey key = config.getKey(i);
            if (key == null
                    || !(key instanceof AEItemKey itemKey)
                    || !(itemKey.getItem() instanceof ItemUpgrade itemUpgrade)) {
                continue;
            }
            Upgrade upgrade = itemUpgrade.getUpgradeType(null);
            if (!tile.supportsUpgrade(upgrade)) {
                continue;
            }
            int current = component.getUpgrades(upgrade);
            int needed = upgrade.getMax() - current;
            if (needed <= 0) {
                continue;
            }
            long extracted = inventory.extract(
                    itemKey,
                    needed,
                    Actionable.SIMULATE,
                    source);
            int toAdd = (int) Math.min(extracted, needed);
            if (toAdd <= 0) {
                continue;
            }
            component.addUpgrades(upgrade, toAdd);
            inventory.extract(
                    itemKey,
                    toAdd,
                    Actionable.MODULATE,
                    source);
        }
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(
                key -> key instanceof AEItemKey itemKey ? itemKey.getItem() instanceof ItemUpgrade : false, is, 63);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is) {
        return UpgradeInventories.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, @NotNull List<Component> lines,
            @NotNull TooltipFlag adv) {
        if (getLinkedPosition(stack) == null) {
            lines.add(Tooltips.of(GuiText.Unlinked, Tooltips.RED));
        } else {
            lines.add(Tooltips.of(GuiText.Linked, Tooltips.GREEN));
        }
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack arg0) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack arg0, FuzzyMode arg1) {
    }
}
