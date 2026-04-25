package astral_mekanism.block.gui.astralmachine;

import astral_mekanism.block.blockentity.prefab.BEAbstractAntiprotonicNucleosynthesizer;
import astral_mekanism.block.gui.prefab.GuiItemGasToItemMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEAntiprotonicNucleoSynthesizer<BE extends BEAbstractAntiprotonicNucleosynthesizer<BE>> extends GuiItemGasToItemMachine<BE> {

    public GuiAMEAntiprotonicNucleoSynthesizer(
            MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    public MekanismJEIRecipeType<?> jei() {
        return MekanismJEIRecipeType.NUCLEOSYNTHESIZING;
    }
    
}
