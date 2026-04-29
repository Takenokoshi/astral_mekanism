package astral_mekanism.block.gui.appliedmachine;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedSmelter;
import astral_mekanism.block.gui.appliedmachine.prefab.GuiAppliedSingleToSingleEnergizedMachine;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedSmelter extends GuiAppliedSingleToSingleEnergizedMachine<BEAppliedSmelter> {

    public GuiAppliedSmelter(MekanismTileContainer<BEAppliedSmelter> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiMEKeySlot(this, 141, 34, tile::getMeStorage, () -> tile.xpKey));
    }

    @Override
    protected MekanismJEIRecipeType<?>[] getJEIJeiRecipeTypes() {
        return new MekanismJEIRecipeType<?>[] { AMEJEIRecipeType.ESSENTIAL_SMELTING, MekanismJEIRecipeType.SMELTING };
    }
}
