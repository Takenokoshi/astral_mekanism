package astral_mekanism.inventory.slot;

import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.IContentsListener;
import mekanism.common.inventory.slot.InputInventorySlot;
import net.minecraft.world.item.ItemStack;

/**
 * Variant of {@link InputInventorySlot} whose insertion limit is bound to the amount required by the
 * currently-matching Essential Crafter recipe at this slot's position, instead of the inserted item's real max
 * stack size.
 * <p>
 * A plain {@link InputInventorySlot} caps insertion at {@code stack.getMaxStackSize()} (see
 * {@link mekanism.common.inventory.slot.BasicInventorySlot#getLimit}). Since several Essential Crafter recipes
 * require the same item at multiple distinct slot positions but in different amounts (e.g. 1 in one slot, 64 in
 * another), that default lets automation (including AE2 autocrafting) dump more of a shared ingredient into a
 * single slot than the recipe actually needs there, leaving sibling slots that need the same item empty and the
 * recipe permanently unmatched. Capping the limit to the recipe's exact amount makes any surplus insertion
 * overflow to the next compatible slot instead, and also makes the limit independent of mods that change an
 * item's real stack size (e.g. Bigger Stacks).
 */
public class EssentialCrafterInputSlot extends InputInventorySlot {

    private final Function<@NotNull ItemStack, Integer> maxRecipeAmount;

    public static EssentialCrafterInputSlot at(Predicate<@NotNull ItemStack> insertPredicate, Predicate<@NotNull ItemStack> isItemValid,
            Function<@NotNull ItemStack, Integer> maxRecipeAmount, @Nullable IContentsListener listener, int x, int y) {
        return new EssentialCrafterInputSlot(insertPredicate, isItemValid, maxRecipeAmount, listener, x, y);
    }

    protected EssentialCrafterInputSlot(Predicate<@NotNull ItemStack> insertPredicate, Predicate<@NotNull ItemStack> isItemValid,
            Function<@NotNull ItemStack, Integer> maxRecipeAmount, @Nullable IContentsListener listener, int x, int y) {
        super(insertPredicate, isItemValid, listener, x, y);
        this.maxRecipeAmount = maxRecipeAmount;
    }

    @Override
    public int getLimit(ItemStack stack) {
        if (!stack.isEmpty()) {
            int recipeAmount = maxRecipeAmount.apply(stack);
            if (recipeAmount > 0) {
                return recipeAmount;
            }
        }
        return super.getLimit(stack);
    }
}
