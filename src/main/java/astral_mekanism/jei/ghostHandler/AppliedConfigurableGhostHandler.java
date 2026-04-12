package astral_mekanism.jei.ghostHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEKey;
import astral_mekanism.block.gui.element.aeKey.GuiAEKeySlotTarget;
import astral_mekanism.block.gui.prefab.GuiAppliedConfigurableBE;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;

public class AppliedConfigurableGhostHandler<GUI extends GuiAppliedConfigurableBE<?, ?>>
        implements IGhostIngredientHandler<GUI> {

    @Override
    public <I> List<Target<I>> getTargetsTyped(GUI gui, ITypedIngredient<I> ingredient, boolean doStart) {
        AEKey key = convertToAEKey(ingredient);
        if (key == null) {
            return Collections.emptyList();
        }
        List<Target<I>> list = new ArrayList<>();
        for (var slot : gui.getAeKeySlots()) {
            if (slot.slot.accept(key)) {
                list.add(new GuiAEKeySlotTarget<>(slot));
            }
        }
        return list;
    }

    @Override
    public void onComplete() {
    }

    @Nullable
    public static AEKey convertToAEKey(ITypedIngredient<?> ingredient) {
        Object obj = ingredient.getIngredient();
        return GuiAEKeySlotTarget.convertToAEKey(obj);
    }
}
