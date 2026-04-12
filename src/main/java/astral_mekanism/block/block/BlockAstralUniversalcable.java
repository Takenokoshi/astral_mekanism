package astral_mekanism.block.block;

import astral_mekanism.registries.AMETileEntityTypes;
import mekanism.common.block.transmitter.BlockUniversalCable;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.CableTier;
import mekanism.common.tile.transmitter.TileEntityUniversalCable;

public class BlockAstralUniversalcable extends BlockUniversalCable {

    public BlockAstralUniversalcable(CableTier tier) {
        super(tier);
    }

    @Override
    public TileEntityTypeRegistryObject<TileEntityUniversalCable> getTileType() {
        return AMETileEntityTypes.ASTRAL_UNIVERSAL_CABLE;
    }

}
