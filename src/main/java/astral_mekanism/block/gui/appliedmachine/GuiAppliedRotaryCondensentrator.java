package astral_mekanism.block.gui.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedRotaryCondensentrator;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedRotaryCondensentrator
        extends GuiAppliedSingleToSingleEnergizedMachine<BEAppliedRotaryCondensentrator> {

    public GuiAppliedRotaryCondensentrator(MekanismTileContainer<BEAppliedRotaryCondensentrator> container,
            Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType[] { MekanismJEIRecipeType.CONDENSENTRATING,
                MekanismJEIRecipeType.DECONDENSENTRATING };
    }

}
