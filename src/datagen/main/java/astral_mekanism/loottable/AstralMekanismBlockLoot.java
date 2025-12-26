package astral_mekanism.loottable;

import java.util.Set;

import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AstralMekanismMachines;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;

public class AstralMekanismBlockLoot extends BlockLootSubProvider {

    public AstralMekanismBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (MachineRegistryObject<?, ?, ?, ?> machine : AstralMekanismMachines.MACHINES.getMachines()) {
            add(machine.getBlock(), block -> createSingleItemTable(machine.asItem())
                    .apply(CopyNbtFunction.copyData(
                            ContextNbtProvider.BLOCK_ENTITY).copy("mekanism:energy", "mekanism:energy")));
        }
    }

}
