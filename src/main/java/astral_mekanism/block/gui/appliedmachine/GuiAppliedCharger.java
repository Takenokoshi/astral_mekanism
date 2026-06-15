package astral_mekanism.block.gui.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedCharger;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedCharger extends GuiAppliedSingleToSingleEnergizedMachine<BEAppliedCharger> {

    public GuiAppliedCharger(MekanismTileContainer<BEAppliedCharger> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { AMEJEIRecipeType.AE_CHARGER };
    }

}
