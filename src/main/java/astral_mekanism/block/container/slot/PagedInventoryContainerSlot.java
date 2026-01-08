package astral_mekanism.block.container.slot;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.inventory.warning.ISupportsWarning;
import net.minecraft.world.item.ItemStack;

public class PagedInventoryContainerSlot extends InventoryContainerSlot {
    private final SelectedWindowData windowData;

    public PagedInventoryContainerSlot(BasicInventorySlot slot, int x, int y, ContainerSlotType slotType,
            @Nullable SlotOverlay slotOverlay, @Nullable Consumer<ISupportsWarning<?>> warningAdder,
            Consumer<ItemStack> uncheckedSetter, SelectedWindowData windowData) {
        super(slot, x, y, slotType, slotOverlay, warningAdder, uncheckedSetter);
        this.windowData = windowData;
    }

    @Override
    public boolean exists(@Nullable SelectedWindowData windowData) {
        return this.windowData.equals(windowData);
    }

}
