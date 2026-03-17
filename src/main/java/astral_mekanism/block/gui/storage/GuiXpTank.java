package astral_mekanism.block.gui.storage;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.storage.BEXpTank;
import astral_mekanism.network.to_server.PacketGuiXpTank;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiXpTank extends GuiConfigurableTile<BEXpTank, MekanismTileContainer<BEXpTank>> {

    public GuiXpTank(MekanismTileContainer<BEXpTank> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInfusionGauge(tile::getInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.STANDARD, this, 154, 13));
        addRenderableWidget(new MekanismButton(this, 4, 18, 72, 12, Component.literal("Store 10 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), false, 10)),
                null));
        addRenderableWidget(new MekanismButton(this, 4, 36, 72, 12, Component.literal("Store 100 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), false, 100)),
                null));
        addRenderableWidget(new MekanismButton(this, 4, 54, 72, 12, Component.literal("Store 1000 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), false, 1000)),
                null));
        addRenderableWidget(new MekanismButton(this, 80, 18, 72, 12, Component.literal("Receive 10 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), true, 10)),
                null));
        addRenderableWidget(new MekanismButton(this, 80, 36, 72, 12, Component.literal("Receive 100 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), true, 100)),
                null));
        addRenderableWidget(new MekanismButton(this, 80, 54, 72, 12, Component.literal("Receive 1000 level."),
                () -> AstralMekanism.packetHandler().sendToServer(new PacketGuiXpTank(tile.getBlockPos(), true, 1000)),
                null));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
