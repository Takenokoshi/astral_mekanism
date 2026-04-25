package astral_mekanism.item.recipecard;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.items.AEBaseItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public abstract class RecipeCardItem<RECIPE extends Recipe<?>> extends AEBaseItem implements ICellWorkbenchItem {

    public RecipeCardItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }
}
