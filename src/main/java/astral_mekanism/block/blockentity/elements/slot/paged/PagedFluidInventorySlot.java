package astral_mekanism.block.blockentity.elements.slot.paged;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.container.slot.PagedInventoryContainerSlot;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class PagedFluidInventorySlot extends FluidInventorySlot implements IPagedSlot {

    private final int page;
    private final int x;
    private final int y;

    protected PagedFluidInventorySlot(IExtendedFluidTank fluidTank, Predicate<@NotNull ItemStack> canExtract,
            Predicate<@NotNull ItemStack> canInsert, Predicate<@NotNull ItemStack> validator,
            @Nullable IContentsListener listener, int x, int y, int page) {
        super(fluidTank, canExtract, canInsert, validator, listener, x, y);
        this.page = page;
        this.x = x;
        this.y = y;
    }

    public static PagedFluidInventorySlot input(IExtendedFluidTank fluidTank, @Nullable IContentsListener listener,
            int x, int y, int page) {
        Objects.requireNonNull(fluidTank, "Fluid tank cannot be null");
        return new PagedFluidInventorySlot(fluidTank, alwaysFalse, getInputPredicate(fluidTank),
                stack -> FluidUtil.getFluidHandler(stack).isPresent(), listener, x, y, page);
    }

    public static PagedFluidInventorySlot fill(IExtendedFluidTank fluidTank, @Nullable IContentsListener listener,
            int x, int y, int page) {
        Objects.requireNonNull(fluidTank, "Fluid tank cannot be null");
        return new PagedFluidInventorySlot(fluidTank, alwaysFalse, stack -> {
            Optional<IFluidHandlerItem> cap = FluidUtil.getFluidHandler(stack).resolve();
            if (cap.isPresent()) {
                IFluidHandlerItem fluidHandlerItem = cap.get();
                for (int tank = 0; tank < fluidHandlerItem.getTanks(); tank++) {
                    FluidStack fluidInTank = fluidHandlerItem.getFluidInTank(tank);
                    if (!fluidInTank.isEmpty()
                            && fluidTank.insert(fluidInTank, Action.SIMULATE, AutomationType.INTERNAL)
                                    .getAmount() < fluidInTank.getAmount()) {
                        return true;
                    }
                }
            }
            return false;
        }, stack -> {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }, listener, x, y, page);
    }

    public static PagedFluidInventorySlot drain(IExtendedFluidTank fluidTank, @Nullable IContentsListener listener,
            int x,
            int y, int page) {
        Objects.requireNonNull(fluidTank, "Fluid handler cannot be null");
        return new PagedFluidInventorySlot(fluidTank, alwaysFalse, stack -> {
            LazyOptional<IFluidHandlerItem> cap = FluidUtil
                    .getFluidHandler(stack.getCount() > 1 ? stack.copyWithCount(1) : stack);
            if (cap.isPresent()) {
                FluidStack fluidInTank = fluidTank.getFluid();
                if (fluidInTank.isEmpty()) {
                    return true;
                }
                IFluidHandlerItem itemFluidHandler = cap.orElseThrow(MekanismUtils.MISSING_CAP_ERROR);
                return itemFluidHandler.fill(fluidInTank.copy(), FluidAction.SIMULATE) > 0;
            }
            return false;
        }, stack -> isNonFullFluidContainer(FluidUtil.getFluidHandler(stack)), listener, x, y, page);
    }

    private static boolean isNonFullFluidContainer(LazyOptional<IFluidHandlerItem> capability) {
        Optional<IFluidHandlerItem> cap = capability.resolve();
        if (cap.isPresent()) {
            IFluidHandlerItem fluidHandler = cap.get();
            for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
                if (fluidHandler.getFluidInTank(tank).getAmount() < fluidHandler.getTankCapacity(tank)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public @Nullable PagedInventoryContainerSlot createContainerSlot() {
        return new PagedInventoryContainerSlot(this, x, y, getSlotType(), getSlotOverlay(), null,
                this::setStackUnchecked, page);
    }

}
