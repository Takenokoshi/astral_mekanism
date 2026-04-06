package astral_mekanism.block.gui.factory;

import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.element.PagedGuiProgress;
import astral_mekanism.jei.AMEJEIRecipeType;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class GuiEnergizedSmeltingFactory<BE extends BlockEntityRecipeFactory<SmeltingRecipe, BE> & IEnergizedSmeltingFactory<BE>>
        extends GuiAstralMekanismFactory<BE> {

    public GuiEnergizedSmeltingFactory(ContainerAstralMekanismFactory<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        for (int index = 0; index < tile.tier.processes; index++) {
            int page = tile.getPageByIndex(index);
            int x = tile.getXByIndex(index) + 4;
            int y = tile.getY() + 20;
            int cacheIndex = index;
            addRenderableWidget(
                    new PagedGuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y, page))
                    .jeiCategories(AMEJEIRecipeType.ESSENTIAL_SMELTING, MekanismJEIRecipeType.SMELTING);
        }
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, imageWidth - 36, 36))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE, 0));
        addRenderableWidget(new GuiGasMode(this, imageWidth - 36, 80, true, tile::getGasMode, tile.getBlockPos(), 0));
    }

}
