package astral_mekanism.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class XpCrystalItem extends Item {

    public XpCrystalItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.9f).alwaysEat().fast().build()));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
}
