package astral_mekanism.block.container.prefab;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.basemachine.BEAMEFormulaicAssemblicator;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

// for jei transfer handler
public class ContainerAMEFormulaicAssemblicator<BE extends BEAMEFormulaicAssemblicator>
        extends ContainerMachineCustomSize<BE> {

    public ContainerAMEFormulaicAssemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BE tile) {
        super(type, id, inv, tile);
    }

}
