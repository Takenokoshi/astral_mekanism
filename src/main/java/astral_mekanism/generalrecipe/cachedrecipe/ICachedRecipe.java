package astral_mekanism.generalrecipe.cachedrecipe;

import net.minecraft.world.item.crafting.Recipe;

public interface ICachedRecipe<RECIPE extends Recipe<?>> {
    public void loadSavedOperatingTicks(int operatingTicks);

    public void process();

    public boolean isInputValid();

    public RECIPE getRecipe();
}
