package astral_mekanism.recipes.output;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;

public record DoubleItemStackOutput(@NotNull ItemStack itemA, @NotNull ItemStack itemB) {
    public DoubleItemStackOutput{}
}
