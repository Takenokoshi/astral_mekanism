package astral_mekanism.block.container.astralmachine;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.astralmachine.BEAstralFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerMachineCustomSize;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAstralFAssemblicator extends ContainerMachineCustomSize<BEAstralFormulaicAssemblicator> {

    public ContainerAstralFAssemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BEAstralFormulaicAssemblicator tile) {
        super(type, id, inv, tile);
    }
    
}
