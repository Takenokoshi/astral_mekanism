package astral_mekanism.block.blockentity.enchantedmachine;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEAirCompressor;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedAirCompressor extends BEAMEAirCompressor {

    public BEEnchantedAirCompressor(IBlockProvider blockProvider,BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                COMPRESSED_AIR_STACK
                        .setAmount(2000l * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                                .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
            }

        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            COMPRESSED_AIR_STACK.setAmount(2000l << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK)));
        }
    }

}
