package astral_mekanism.block.gui.appliedmachine;

import java.util.EnumMap;
import java.util.List;

import astral_mekanism.AMEConstants;
import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.prefab.BEAbstractAppliedMixingReactor;
import astral_mekanism.block.gui.element.EnumToggleButton;
import astral_mekanism.block.gui.element.GuiMEKeySlot;
import astral_mekanism.enums.AppliedMixingReactorMode;
import astral_mekanism.network.to_server.PacketGuiSetLong;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.InputValidator;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiAppliedMixingReactor<BE extends BEAbstractAppliedMixingReactor>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {
    private GuiTextField field;
    private final EnumMap<AppliedMixingReactorMode, ResourceLocation> textureMap;

    public GuiAppliedMixingReactor(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.textureMap = new EnumMap<>(AppliedMixingReactorMode.class);
        textureMap.put(AppliedMixingReactorMode.MIXED_ONLY, AMEConstants.rl("textures/item/gui/amr_mixed_only.png"));
        textureMap.put(AppliedMixingReactorMode.MIXED_PRIORITIZE, AMEConstants.rl("textures/item/gui/amr_mixed_pr.png"));
        textureMap.put(AppliedMixingReactorMode.UNMIXED_PRIORITIZE, AMEConstants.rl("textures/item/gui/amr_unmixed_pr.png"));
        textureMap.put(AppliedMixingReactorMode.UNMIXED_ONLY, AMEConstants.rl("textures/item/gui/amr_unmixed_only.png"));
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInnerScreen(this, 42, 4, 126, 30, () -> {
            return List.of(this.title, GeneratorsLang.GAS_BURN_RATE.translate(this.tile.getEfficiency()));
        })).clearFormat().jeiCategories(MekanismJEIRecipeType.findType(tile.getJEICategoryName()));
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
        addRenderableWidget(new GuiDownArrow(this, 20, 40));
        addRenderableWidget(new GuiUpArrow(this, 157, 40));
        addRenderableWidget(new GuiMEKeySlot(this, 6, 17, tile::getMeStorage, () -> tile.leftFuelKey));
        addRenderableWidget(new GuiMEKeySlot(this, 24, 17, tile::getMeStorage, () -> tile.rightFuelKey));
        addRenderableWidget(new GuiMEKeySlot(this, 15, 53, tile::getMeStorage, () -> tile.mixedFuelKey));
        addRenderableWidget(new GuiMEKeySlot(this, 152, 17, tile::getMeStorage, () -> tile.steamKey));
        addRenderableWidget(new GuiMEKeySlot(this, 152, 53, tile::getMeStorage, () -> tile.waterKey));
        addRenderableWidget(new EnumToggleButton<>(this, 80, 53, 18, 18, 16, 16,
                tile::getMode, textureMap,
                () -> Mekanism.packetHandler().sendToServer(new PacketGuiInteract(GuiInteraction.NEXT_MODE, tile)),
                () -> Mekanism.packetHandler().sendToServer(new PacketGuiInteract(GuiInteraction.PREVIOUS_MODE, tile)),
                getOnHover(() -> tile.getMode().langEntry.translate())));
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
