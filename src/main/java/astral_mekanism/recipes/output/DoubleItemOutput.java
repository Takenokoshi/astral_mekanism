package astral_mekanism.recipes.output;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;

public record DoubleItemOutput(@NotNull ItemStack itemA, @NotNull ItemStack itemB) {
    public DoubleItemOutput{}
}
