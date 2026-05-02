package astral_mekanism.item.recipecard;

import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.world.item.ItemStack;

public abstract class ChemicalIngredientCardItem extends AbstractCardItem {

    public ChemicalIngredientCardItem(Properties properties) {
        super(properties);
    }

    public abstract MekanismKey getKey(ItemStack is);

}
