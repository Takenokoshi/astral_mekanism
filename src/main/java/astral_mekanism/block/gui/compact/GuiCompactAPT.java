package astral_mekanism.block.gui.compact;

import astral_mekanism.block.blockentity.interf.IItemGasToItemMachine;
import astral_mekanism.block.gui.prefab.GuiItemGasToItemMachine;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiCompactAPT<BE extends TileEntityConfigurableMachine & IItemGasToItemMachine<BE, ?>> extends GuiItemGasToItemMachine<BE> {

    public GuiCompactAPT(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    public MekanismJEIRecipeType<?> jei() {
        return EMJEI.APT;
    }
    
}
