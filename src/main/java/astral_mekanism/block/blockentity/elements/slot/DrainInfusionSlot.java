package astral_mekanism.block.blockentity.elements.slot;

import org.jetbrains.annotations.Nullable;

import mekanism.api.IContentsListener;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import net.minecraft.world.item.ItemStack;

public class DrainInfusionSlot extends ChemicalInventorySlot<InfuseType, InfusionStack> {

    public DrainInfusionSlot(IChemicalTank<InfuseType, InfusionStack> chemicalTank,
            @Nullable IContentsListener listener, int x, int y) {
        super(chemicalTank, () -> null, alwaysTrue, alwaysFalse, getDrainInsertPredicate(chemicalTank, DrainInfusionSlot::getCapability), listener, x, y);
    }

    

    protected static @Nullable IChemicalHandler<InfuseType, InfusionStack> getCapability(ItemStack stack) {
        return (IInfusionHandler) getCapability(stack, Capabilities.INFUSION_HANDLER);
    }

    @Override
    protected @Nullable IChemicalHandler<InfuseType, InfusionStack> getCapability() {
        return getCapability(getStack());
    }

}
