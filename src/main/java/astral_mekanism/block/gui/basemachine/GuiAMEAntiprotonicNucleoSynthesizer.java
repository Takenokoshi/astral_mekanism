package astral_mekanism.block.gui.basemachine;

import astral_mekanism.block.blockentity.basemachine.BEAMEAntiprotonicNucleosynthesizer;
import astral_mekanism.block.gui.prefab.GuiItemGasToItemMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEAntiprotonicNucleoSynthesizer<BE extends BEAMEAntiprotonicNucleosynthesizer<BE>> extends GuiItemGasToItemMachine<BE> {

    public GuiAMEAntiprotonicNucleoSynthesizer(
            MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    public MekanismJEIRecipeType<?> jei() {
        return MekanismJEIRecipeType.NUCLEOSYNTHESIZING;
    }
    
}
