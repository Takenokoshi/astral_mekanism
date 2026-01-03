package astral_mekanism.block.gui.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.prefab.BlockEntityRecipeMachine;
import astral_mekanism.jei.AstralMekanismJEIPlugin;
import astral_mekanism.jei.AstralMekanismJEIRecipeType;
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
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.fml.ModList;

public class GuiEssentialEnergizedSmelter<BE extends BlockEntityRecipeMachine<SmeltingRecipe> & IEssentialEnergizedSmelter<BE>>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiEssentialEnergizedSmelter(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.STANDARD, this, 137, 13))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiProgress(tile::getProgressScaled, ProgressType.BAR, this, 82, 38))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT))
                .jeiCategories(MekanismJEIRecipeType.SMELTING);
        addRenderableWidget(new MekanismImageButton(this, 117, 13, 18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/item/experience_bottle.png"), this::onPush));
        if (ModList.get().isLoaded("jei")) {
            addRenderableWidget(new MekanismImageButton(
                    this,
                    90, 53,
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

    public static void connectJEI() {
        IJeiRuntime runtime = AstralMekanismJEIPlugin.getRuntime();
        if (runtime == null)
            return;
        runtime.getRecipesGui().showTypes(
                List.of(AstralMekanismJEIRecipeType.ESSENTIAL_SMELTING));
    }

}
