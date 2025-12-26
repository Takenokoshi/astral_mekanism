package astral_mekanism.loottable;

import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.common.loot.table.BaseBlockLootTables;

public class AstralMekanismBlockLoot extends BaseBlockLootTables {

    public AstralMekanismBlockLoot() {
        super();
    }

    @Override
    protected void generate() {
        dropSelfWithContents(AstralMekanismBlocks.BLOCKS.getAllBlocks());
        dropSelfWithContents(AstralMekanismMachines.MACHINES.blockRegister.getAllBlocks());
    }

}
