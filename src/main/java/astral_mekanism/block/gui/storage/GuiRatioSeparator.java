package astral_mekanism.block.gui.storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.storage.BERatioSeparator;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.gui.element.PagedGuiFluidGauge;
import astral_mekanism.block.gui.element.PagedGuiGasGauge;
import astral_mekanism.block.gui.element.PagedGuiInfusiongauge;
import astral_mekanism.block.gui.element.PagedGuiInnerScreen;
import astral_mekanism.block.gui.element.PagedGuiPigmentGauge;
import astral_mekanism.block.gui.element.PagedGuiSlurryGauge;
import astral_mekanism.block.gui.element.PagedGuiTextField;
import astral_mekanism.block.gui.prefab.GuiPagedMachine;
import astral_mekanism.network.to_server.PacketGuiRatioSeparator;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.text.BackgroundType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.util.text.InputValidator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiRatioSeparator extends GuiPagedMachine<BERatioSeparator> {

    public GuiRatioSeparator(ContainerPagedMachine<BERatioSeparator> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    protected int getMaxPage() {
        return 6;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new PagedGuiFluidGauge(() -> tile.inputFluidTank, () -> tile.getFluidTanks(null),
                GaugeType.STANDARD, this, 7, 13, 1));
        addRenderableWidget(new PagedGuiFluidGauge(() -> tile.outputFluidTankA, () -> tile.getFluidTanks(null),
                GaugeType.SMALL, this, 88, 13, 1));
        addRenderableWidget(new PagedGuiFluidGauge(() -> tile.outputFluidTankB, () -> tile.getFluidTanks(null),
                GaugeType.SMALL, this, 88, 43, 1));
        addRenderableWidget(new PagedGuiGasGauge(() -> tile.inputGasTank, () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 7, 13, 2));
        addRenderableWidget(new PagedGuiGasGauge(() -> tile.outputGasTankA, () -> tile.getGasTanks(null),
                GaugeType.SMALL, this, 88, 13, 2));
        addRenderableWidget(new PagedGuiGasGauge(() -> tile.outputGasTankB, () -> tile.getGasTanks(null),
                GaugeType.SMALL, this, 88, 43, 2));
        addRenderableWidget(new PagedGuiInfusiongauge(() -> tile.inputInfusionTank, () -> tile.getInfusionTanks(null),
                GaugeType.STANDARD, this, 7, 13, 3));
        addRenderableWidget(new PagedGuiInfusiongauge(() -> tile.outputInfusionTankA, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, 88, 13, 3));
        addRenderableWidget(new PagedGuiInfusiongauge(() -> tile.outputInfusionTankB, () -> tile.getInfusionTanks(null),
                GaugeType.SMALL, this, 88, 43, 3));
        addRenderableWidget(new PagedGuiPigmentGauge(() -> tile.inputPigmentTank, () -> tile.getPigmentTanks(null),
                GaugeType.STANDARD, this, 7, 13, 4));
        addRenderableWidget(new PagedGuiPigmentGauge(() -> tile.outputPigmentTankA, () -> tile.getPigmentTanks(null),
                GaugeType.SMALL, this, 88, 13, 4));
        addRenderableWidget(new PagedGuiPigmentGauge(() -> tile.outputPigmentTankB, () -> tile.getPigmentTanks(null),
                GaugeType.SMALL, this, 88, 43, 4));
        addRenderableWidget(new PagedGuiSlurryGauge(() -> tile.inputSlurryTank, () -> tile.getSlurryTanks(null),
                GaugeType.STANDARD, this, 7, 13, 5));
        addRenderableWidget(new PagedGuiSlurryGauge(() -> tile.outputSlurryTankA, () -> tile.getSlurryTanks(null),
                GaugeType.SMALL, this, 88, 13, 5));
        addRenderableWidget(new PagedGuiSlurryGauge(() -> tile.outputSlurryTankB, () -> tile.getSlurryTanks(null),
                GaugeType.SMALL, this, 88, 43, 5));

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.itemAValue + tile.itemBValue))), 0));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.itemAValue))), 0));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 0))
                .configureDigitalInput(v -> sendPacket(TransmissionType.ITEM, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(10);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.itemBValue))), 0));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 0))
                .configureDigitalInput(v -> sendPacket(TransmissionType.ITEM, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(10);

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.fluidAValue + tile.fluidBValue))), 1));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.fluidAValue))), 1));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 1))
                .configureDigitalInput(v -> sendPacket(TransmissionType.FLUID, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(10);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.fluidBValue))), 1));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 1))
                .configureDigitalInput(v -> sendPacket(TransmissionType.FLUID, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(10);

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.gasAValue + tile.gasBValue))), 2));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.gasAValue))), 2));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 2))
                .configureDigitalInput(v -> sendPacket(TransmissionType.GAS, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.gasBValue))), 2));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 2))
                .configureDigitalInput(v -> sendPacket(TransmissionType.GAS, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.infusionAValue + tile.infusionBValue))), 3));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.infusionAValue))), 3));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 3))
                .configureDigitalInput(v -> sendPacket(TransmissionType.INFUSION, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.infusionBValue))), 3));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 3))
                .configureDigitalInput(v -> sendPacket(TransmissionType.INFUSION, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.pigmentAValue + tile.pigmentBValue))), 4));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.pigmentAValue))), 4));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 4))
                .configureDigitalInput(v -> sendPacket(TransmissionType.PIGMENT, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.pigmentBValue))), 4));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 4))
                .configureDigitalInput(v -> sendPacket(TransmissionType.PIGMENT, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);

        addRenderableWidget(new PagedGuiInnerScreen(this, 25, 13, 63, 9,
                () -> List.of(Component.literal("Total:" + (tile.slurryAValue + tile.slurryBValue))), 5));
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 13, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.slurryAValue))), 5));
        addRenderableWidget(new PagedGuiTextField(this, 106, 22, 63, 9, 5))
                .configureDigitalInput(v -> sendPacket(TransmissionType.SLURRY, true, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);
        addRenderableWidget(new PagedGuiInnerScreen(this, 106, 43, 63, 9,
                () -> List.of(Component.literal("Rate:" + (tile.slurryBValue))), 5));
        addRenderableWidget(new PagedGuiTextField(this, 106, 52, 63, 9, 5))
                .configureDigitalInput(v -> sendPacket(TransmissionType.SLURRY, false, v))
                .setInputValidator(InputValidator.DIGIT)
                .setBackground(BackgroundType.INNER_SCREEN)
                .setMaxLength(19);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        drawString(guiGraphics, getPageDiscription(), 100, -30, 0xffffff);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    private Component getPageDiscription() {
        switch (page) {
            case 1:
                return Component.literal("Fluid");
            case 2:
                return Component.literal("Gas");
            case 3:
                return Component.literal("Infuse Type");
            case 4:
                return Component.literal("Pigment");
            case 5:
                return Component.literal("Slurry");
            default:
                return Component.literal("Item");
        }
    }

    private void sendPacket(TransmissionType type, boolean isA, String value) {
        long v = -1;
        try {
            v = Long.parseLong(value);
        } catch (Exception e) {
        }
        if (v > -1) {
            AstralMekanism.packetHandler().sendToServer(new PacketGuiRatioSeparator(tile.getBlockPos(), type, isA, v));
        }
    }

}
