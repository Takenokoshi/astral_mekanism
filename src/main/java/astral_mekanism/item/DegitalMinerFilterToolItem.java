package astral_mekanism.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import mekanism.common.content.filter.SortableFilterManager;
import mekanism.common.content.miner.MinerFilter;
import mekanism.common.content.miner.MinerItemStackFilter;
import mekanism.common.tile.TileEntityBoundingBlock;
import mekanism.common.tile.machine.TileEntityDigitalMiner;
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

public class DegitalMinerFilterToolItem extends Item implements ICellWorkbenchItem {

    public DegitalMinerFilterToolItem(Properties p_41383_) {
        super(p_41383_);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Level world = context.getLevel();
            BlockEntity be = WorldUtils.getTileEntity(world, context.getClickedPos());
            if (be instanceof TileEntityDigitalMiner miner) {
                addFilter(context.getItemInHand(), player, miner);
                return InteractionResult.sidedSuccess(world.isClientSide);
            } else if (be instanceof TileEntityBoundingBlock boundingBlock
                    && boundingBlock.getMainTile() instanceof TileEntityDigitalMiner miner2) {
                addFilter(context.getItemInHand(), player, miner2);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private void addFilter(ItemStack stack, Player player, TileEntityDigitalMiner miner) {
        Level level = miner.getLevel();
        if (level == null || level.isClientSide || level != player.level()
                || !(stack.getItem() instanceof DegitalMinerFilterToolItem toolItem)) {
            return;
        }
        ConfigInventory inv = toolItem.getConfigInventory(stack);
        if (inv == null || inv.isEmpty()) {
            return;
        }
        SortableFilterManager<MinerFilter<?>> filterManager = miner.getFilterManager();
        inv.keySet().forEach(key -> {
            if (key instanceof AEItemKey itemKey) {
                filterManager.addFilter(new MinerItemStackFilter(itemKey.toStack()));
            }
        });
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEItemKey::is, is);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is) {
        return UpgradeInventories.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, @NotNull List<Component> lines,
            @NotNull TooltipFlag adv) {
        lines.add(Component.literal("Register Items on Cellwork bench."));
        lines.add(Component.literal("Add Itemstack Filters to DegitalMiner (Shift + RightClick)."));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack arg0) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack arg0, FuzzyMode arg1) {
    }

}
