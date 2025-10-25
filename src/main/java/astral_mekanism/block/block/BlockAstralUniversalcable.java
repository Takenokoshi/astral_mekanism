package astral_mekanism.block.block;

import astral_mekanism.registries.AstralMekanismTileEntityTypes;
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
        return AstralMekanismTileEntityTypes.ASTRAL_UNIVERSAL_CABLE;
    }

}
