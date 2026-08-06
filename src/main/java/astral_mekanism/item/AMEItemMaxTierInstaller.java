package astral_mekanism.item;

import appeng.api.integrations.igtooltip.TooltipContext;
import astral_mekanism.AMELang;
import astral_mekanism.AMETier;
import astral_mekanism.block.block.AMEAttributeUpgradeable;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.common.tile.interfaces.ITileDirectional;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AMEItemMaxTierInstaller extends Item {

    public AMEItemMaxTierInstaller(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (world.isClientSide || player == null) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        AMEAttributeUpgradeable upgradeableBlock = Attribute.get(block, AMEAttributeUpgradeable.class);
        if (upgradeableBlock != null) {
            AMETier baseTier = Attribute.getTier(block, AMETier.class);

            if (baseTier == AMETier.ASTRAL) {
                return InteractionResult.PASS;
            }

            if (baseTier == null) {
                baseTier = AMETier.ESSENTIAL;
            }

            AMETier toTier = baseTier;
            BlockState upgradeState = upgradeableBlock.upgradeResult(state, toTier);
            while (toTier != AMETier.ASTRAL) {
                AMEAttributeUpgradeable nextUpgradeable = Attribute.get(upgradeState.getBlock(), AMEAttributeUpgradeable.class);
                if (nextUpgradeable == null) {
                    break;
                }
                upgradeableBlock = nextUpgradeable;
                upgradeState = upgradeableBlock.upgradeResult(upgradeState, toTier);
                toTier = Attribute.getTier(upgradeState.getBlock(), AMETier.class);
                if (toTier == null) {
                    break;
                }
            }

            if (state == upgradeState) {
                return InteractionResult.PASS;
            }

            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
            if (tile instanceof ITierUpgradable tierUpgradable) {
                if (tile instanceof TileEntityMekanism tileMek && !tileMek.playersUsing.isEmpty()) {
                    return InteractionResult.FAIL;
                }
                IUpgradeData upgradeData = tierUpgradable.getUpgradeData();
                if (upgradeData == null) {
                    if (tierUpgradable.canBeUpgraded()) {
                        Mekanism.logger.warn("Got no upgrade data for block {} at position: {} in {} but it said it would be able to provide some.", block, pos, world);
                        return InteractionResult.FAIL;
                    }
                } else {
                    world.setBlockAndUpdate(pos, upgradeState);
                    // TODO: Make it so it doesn't have to be a TileEntityMekanism?
                    TileEntityMekanism upgradedTile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
                    if (upgradedTile == null) {
                        Mekanism.logger.warn("Error upgrading block at position: {} in {}.", pos, world);
                        return InteractionResult.FAIL;
                    } else {
                        if (tile instanceof ITileDirectional directional && directional.isDirectional()) {
                            upgradedTile.setFacing(directional.getDirection());
                        }
                        upgradedTile.parseUpgradeData(upgradeData);
                        upgradedTile.sendUpdatePacket();
                        upgradedTile.setChanged();
                        if (!player.isCreative()) {
                            context.getItemInHand().shrink(1);
                        }
                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, tooltip, p_41424_);
        if(MekKeyHandler.isKeyPressed(MekanismKeyHandler.descriptionKey)) tooltip.add(AMELang.DESCRIPTION_ASTRONOMICAL_MAX_TIER_INSTALLER.translate());
        else tooltip.add(MekanismLang.HOLD_FOR_DESCRIPTION.translateColored(EnumColor.GRAY, EnumColor.AQUA, MekanismKeyHandler.descriptionKey.getTranslatedKeyMessage()));

    }
}
