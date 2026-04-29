package astral_mekanism.block.gui.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedSPS;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedSPS extends GuiAppliedSingleToSingleEnergizedMachine<BEAppliedSPS> {

    public GuiAppliedSPS(MekanismTileContainer<BEAppliedSPS> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.SPS, AMEJEIRecipeType.SPS_RECIPE };
    }

}
