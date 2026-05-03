package astral_mekanism.mixin.minecraft;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.gson.JsonElement;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.Mekanism;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@Mixin(value = RecipeManager.class, remap = true)
public class RecipeManagerMixin {
    @ModifyVariable(method = "apply", at = @At("HEAD"), argsOnly = true)
    private Map<ResourceLocation, JsonElement> astral_mekanism$applyModify(
            Map<ResourceLocation, JsonElement> original) {
        LinkedHashMap<ResourceLocation, JsonElement> result = new LinkedHashMap<>(original);
        String[] gemNames = { "coal", "diamond", "emerald", "fluorite", "lapis_lazuli", "quartz", "redstone" };
        for (String name : gemNames) {
            result.remove(Mekanism.rl("processing/" + name + "/to_ore"));
            result.remove(Mekanism.rl("processing/" + name + "/to_deepslate_ore"));
        }
        String[] stoneNames = { "depthrock", "end_stone", "holystone", "netherrack", "shiverstone" };
        for (String name : stoneNames) {
            result.remove(EvolvedMekanism.rl("processing/fluorite/to_" + name + "_ore"));
        }
        return result;
    }
}
