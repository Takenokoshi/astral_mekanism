package astral_mekanism.block.blockentity.elements.energyContainer;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.interf.IEnergyRequiredRecipeMachine;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.common.block.attribute.AttributeEnergy;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;

public class EnergyRequiredRecipeMachineEnergyContainer<BE extends TileEntityMekanism & IEnergyRequiredRecipeMachine>
        extends MachineEnergyContainer<BE> {

    public static <BE2 extends TileEntityMekanism & IEnergyRequiredRecipeMachine> EnergyRequiredRecipeMachineEnergyContainer<BE2> createInput(
            BE2 be, IContentsListener listener) {
        AttributeEnergy attributeEnergy = validateBlock(be);
        return new EnergyRequiredRecipeMachineEnergyContainer<BE2>(attributeEnergy.getStorage(),
                attributeEnergy.getUsage(), notExternal, alwaysTrue, be, listener);
    }

    private EnergyRequiredRecipeMachineEnergyContainer(FloatingLong maxEnergy, FloatingLong energyPerTick,
            Predicate<@NotNull AutomationType> canExtract, Predicate<@NotNull AutomationType> canInsert, BE tile,
            @Nullable IContentsListener listener) {
        super(maxEnergy, energyPerTick, canExtract, canInsert, tile, listener);
    }

    @Override
    public FloatingLong getBaseEnergyPerTick() {
        return super.getBaseEnergyPerTick().add(tile.getRecipeEnergyRequired());
    }

}
