package astral_mekanism.block.container.prefab;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.block.blockentity.astralmachine.BEAstralFormulaicAssemblicator;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedFormulaicAssemblicator;
import astral_mekanism.block.blockentity.interf.IAMEFormulaicAssemblicator;
import astral_mekanism.block.blockentity.interf.IHasCustomSizeContainer;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialFormulaicAssemblicator;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.world.entity.player.Inventory;

// for jei transfer handler
public class ContainerAMEFormulaicAssemblicator<BE extends TileEntityConfigurableMachine & IAMEFormulaicAssemblicator & IHasCustomSizeContainer>
        extends ContainerMachineCustomSize<BE> {

    public ContainerAMEFormulaicAssemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
            @NotNull BE tile) {
        super(type, id, inv, tile);
    }

    public static class ContainerEssentialFormulaicAseemblicator extends ContainerAMEFormulaicAssemblicator<BEEssentialFormulaicAssemblicator> {

        public ContainerEssentialFormulaicAseemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                @NotNull BEEssentialFormulaicAssemblicator tile) {
            super(type, id, inv, tile);
        }
    }

    public static class ContainerEnchantedFormulaicAssemblicator extends ContainerAMEFormulaicAssemblicator<BEEnchantedFormulaicAssemblicator> {

        public ContainerEnchantedFormulaicAssemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                @NotNull BEEnchantedFormulaicAssemblicator tile) {
            super(type, id, inv, tile);
        }
    }

    public static class ContainerAstralFormulaicAssemblicator extends ContainerAMEFormulaicAssemblicator<BEAstralFormulaicAssemblicator> {

        public ContainerAstralFormulaicAssemblicator(ContainerTypeRegistryObject<?> type, int id, Inventory inv,
                @NotNull BEAstralFormulaicAssemblicator tile) {
            super(type, id, inv, tile);
        }
    }

}
