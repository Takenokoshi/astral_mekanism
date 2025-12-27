package astral_mekanism.loottable;

import java.util.Set;

import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class AstralMekanismBlockLoot extends BlockLootSubProvider {

    public AstralMekanismBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        AstralMekanismMachines.MACHINES.blockRegister.getAllBlocks()
                .forEach(this::dropMachine);
    }

    private void dropMachine(IBlockProvider blockProvider) {
        add(blockProvider.getBlock(), createMachineDrop(blockProvider));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return AstralMekanismMachines.MACHINES.getAllMachines().stream().map(MachineRegistryObject::getBlock).toList();
    }

    protected LootTable.Builder createMachineDrop(IBlockProvider blockProvider) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(blockProvider.getBlock())
                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("mekData",
                                                "BlockEntityTag.mekData",
                                                CopyNbtFunction.MergeStrategy.REPLACE))));
    }

}
