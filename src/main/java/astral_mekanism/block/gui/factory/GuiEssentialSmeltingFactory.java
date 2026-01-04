package astral_mekanism.block.gui.factory;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.base.FactoryGuiHelper;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.normalmachine.GuiEssentialEnergizedSmelter;
import astral_mekanism.network.to_server.PacketGuiEssentialSmelter;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.fml.ModList;

public class GuiEssentialSmeltingFactory<BE extends BlockEntityRecipeFactory<SmeltingRecipe, BE> & IEnergizedSmeltingFactory<BE>>
        extends GuiConfigurableTile<BE, ContainerAstralMekanismFactory<BE>> {

    public GuiEssentialSmeltingFactory(ContainerAstralMekanismFactory<BE> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        imageHeight = FactoryGuiHelper.getALLHeight(tile.tier, tile.getHeightPerProcess());
        imageWidth = FactoryGuiHelper.getALLWidth(tile.tier, tile.getWidthPerProcess(), tile.getSideSpaceWidth());
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 188, 16)
                .warning(WarningType.NOT_ENOUGH_ENERGY,
                        tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY, 0)));
        for (int index = 0; index < tile.tier.processes; index++) {
            int x = FactoryGuiHelper.getXofOneLine(index, tile.tier, tile.getWidthPerProcess(),
                    tile.getSideSpaceWidth()) + 4;
            int y = FactoryGuiHelper.getYofOneLine(index, tile.tier, tile.getHeightPerProcess() + 20);
            int cacheIndex = index;
            addRenderableWidget(
                    new GuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y))
                    .jeiCategories(MekanismJEIRecipeType.SMELTING);
        }
        addRenderableWidget(new MekanismImageButton(this, imageWidth - 24, imageHeight - 18, 18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/item/experience_bottle.png"), this::onPush));
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, imageWidth - 24, imageHeight - 36))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE, 0));
        if (ModList.get().isLoaded("jei")) {
            addRenderableWidget(new MekanismImageButton(
                    this,
                    3, 80,
                    18, 18,
                    16, 16,
                    new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                    GuiEssentialEnergizedSmelter::connectJEI));
        }

    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private void onPush() {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiEssentialSmelter(tile.getBlockPos()));
    }

}
