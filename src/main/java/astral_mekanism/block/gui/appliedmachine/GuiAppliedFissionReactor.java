package astral_mekanism.block.gui.appliedmachine;

import java.util.List;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedFissionReactor;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.network.to_server.PacketGuiSetLong;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.InputValidator;
import mekanism.generators.client.jei.GeneratorsJEIRecipeType;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedFissionReactor
        extends GuiConfigurableTile<BEAppliedFissionReactor, MekanismTileContainer<BEAppliedFissionReactor>> {
    private GuiTextField field;

    public GuiAppliedFissionReactor(MekanismTileContainer<BEAppliedFissionReactor> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInnerScreen(this, 25, 4, 126, 30, () -> {
            return List.of(this.title, GeneratorsLang.GAS_BURN_RATE.translate(this.tile.getEfficiency()));
        }).clearFormat()).jeiCategories(GeneratorsJEIRecipeType.FISSION);
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component temp = MekanismUtils.getTemperatureDisplay(tile.getHeatCapacitor().getTemperature(),
                    TemperatureUnit.KELVIN, true);
            Component transfer = MekanismUtils.getTemperatureDisplay(tile.getLastTransferLoss(),
                    TemperatureUnit.KELVIN, false);
            Component environment = MekanismUtils.getTemperatureDisplay(tile.getLastEnvironmentLoss(),
                    TemperatureUnit.KELVIN, false);
            return List.of(MekanismLang.TEMPERATURE.translate(temp), MekanismLang.TRANSFERRED_RATE.translate(transfer),
                    MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
        addRenderableWidget(new GuiInnerScreen(this, 61, 34, 54, 12));
        field = addRenderableWidget(new GuiTextField(this, 61, 34, 54, 12));
        field.setMaxLength(19);
        field.setInputValidator(InputValidator.DIGIT).configureDigitalInput(this::setEfficiency);
        field.setFocused(true);

        addRenderableWidget(new GuiMEKeySlot(
                this,
                6, 17,
                tile::getMeStorage,
                tile::getFuelkey));
        addRenderableWidget(new GuiDownArrow(this, 11, 40));
        addRenderableWidget(new GuiMEKeySlot(
                this,
                6, 53,
                tile::getMeStorage,
                tile::getWastekey));
        addRenderableWidget(new GuiMEKeySlot(
                this,
                152, 17,
                tile::getMeStorage,
                tile::getHeatedCoolantKey));
        addRenderableWidget(new GuiUpArrow(this, 157, 40));
        addRenderableWidget(new GuiMEKeySlot(
                this,
                152, 53,
                tile::getMeStorage,
                tile::getCooledCoolantKey));
    }

    private void setEfficiency() {
        if (!field.getText().isEmpty()) {
            try {
                AstralMekanism.packetHandler()
                        .sendToServer(new PacketGuiSetLong(0, Long.parseLong(field.getText()), tile.getBlockPos()));
            } catch (NumberFormatException ignored) {
            }
            field.setText("");
        }
    }

}
