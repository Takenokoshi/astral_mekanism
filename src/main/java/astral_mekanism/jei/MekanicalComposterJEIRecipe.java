package astral_mekanism.jei;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.ForgeRegistries;

public record MekanicalComposterJEIRecipe(ItemStack input) {
    public static List<MekanicalComposterJEIRecipe> getRecipes() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(ComposterBlock.COMPOSTABLES::containsKey)
                .map(item -> new MekanicalComposterJEIRecipe(
                        new ItemStack(item, (int) Math.floor(7 / ComposterBlock.COMPOSTABLES.getFloat(item)))))
                .toList();
    }
}
