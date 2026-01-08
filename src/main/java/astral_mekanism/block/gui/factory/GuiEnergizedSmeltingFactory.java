package astral_mekanism.block.gui.factory;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.interf.IEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.interf.IEssentialEnergizedSmelter;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.gui.normalmachine.GuiEssentialEnergizedSmelter;
import astral_mekanism.network.to_server.PacketGuiEssentialSmelter;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.GuiGasMode;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.fml.ModList;

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
            if (tile.getPageByIndex(index) == page) {
                int x = tile.getXByIndex(index) + 4;
                int y = tile.getY() + 20;
                int cacheIndex = index;
                addRenderableWidget(
                        new GuiProgress(() -> tile.getProgressScaled(cacheIndex), ProgressType.DOWN, this, x, y))
                        .jeiCategories(MekanismJEIRecipeType.SMELTING);
            }
        }
        addRenderableWidget(new MekanismImageButton(this, imageWidth - 36, 60, 18, 18, 16, 16,
                new ResourceLocation("minecraft", "textures/item/experience_bottle.png"), this::onPush));
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, imageWidth - 36, 36))
                .warning(WarningType.NO_SPACE_IN_OUTPUT,
                        tile.getWarningCheck(IEssentialEnergizedSmelter.NOT_ENOUGH_INFUSE_OUTPUT_SPACE, 0));
        addRenderableWidget(new GuiGasMode(this, imageWidth - 36, 70, true, tile::getGasMode, tile.getBlockPos(), 0));
        if (ModList.get().isLoaded("jei")) {
            addRenderableWidget(new MekanismImageButton(
                    this,
                    3, 60,
                    18, 18,
                    16, 16,
                    new ResourceLocation("minecraft", "textures/item/knowledge_book.png"),
                    GuiEssentialEnergizedSmelter::connectJEI));
        }
    }

    private void onPush() {
        AstralMekanism.packetHandler().sendToServer(new PacketGuiEssentialSmelter(tile.getBlockPos()));
    }

}
