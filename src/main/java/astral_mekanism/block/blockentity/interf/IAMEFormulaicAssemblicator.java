package astral_mekanism.block.blockentity.interf;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;

public interface IAMEFormulaicAssemblicator {
    MachineEnergyContainer<?> getEnergyContainer();

    void setSavedRecipe(CraftingRecipe recipe);

    CraftingRecipe getSavedRecipe();

    double getScaledProgress();
}
