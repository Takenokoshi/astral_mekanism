package astral_mekanism.mixin.minecraft;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.gson.JsonElement;

import astral_mekanism.AMEConstants;
import astral_mekanism.config.AMEConfig;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import io.github.masyumero.emextras.EMExtras;
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
        result.remove(EvolvedMekanism.rl("control_circuit/creative"));
        result.remove(EMExtras.rl("alloying/circuits/creative_control_circuit"));
        result.remove(EvolvedMekanism.rl("chemical_tank/creative"));
        result.remove(EvolvedMekanism.rl("fluid_tank/creative"));
        result.remove(EvolvedMekanism.rl("bin/creative"));
        result.remove(EvolvedMekanism.rl("energy_cube/creative"));
        result.remove(ResourceLocation.fromNamespaceAndPath("avaritia", "mek_creative_bin"));
        result.remove(ResourceLocation.fromNamespaceAndPath("avaritia", "mek_creative_chemical_tank"));
        result.remove(ResourceLocation.fromNamespaceAndPath("avaritia", "mek_creative_energy_cube"));
        result.remove(ResourceLocation.fromNamespaceAndPath("avaritia", "mek_creative_fluid_tank"));

        result.remove(Mekanism.rl("processing/netherite/dust_to_ancient_debris"));

        if (AMEConfig.MAKE_RECIPE_DIFFICULT.get()) {
            result.remove(Mekanism.rl("metallurgic_infusing/alloy/infused"));
            result.remove(Mekanism.rl("metallurgic_infusing/alloy/reinforced"));
            result.remove(Mekanism.rl("metallurgic_infusing/alloy/atomic"));


            result.remove(EMExtras.rl("alloying/circuits/basic_control_circuit"));
            result.remove(EMExtras.rl("alloying/circuits/advanced_control_circuit"));
            result.remove(EMExtras.rl("alloying/circuits/elite_control_circuit"));
            result.remove(EMExtras.rl("alloying/circuits/ultimate_control_circuit"));

            result.remove(Mekanism.rl("control_circuit/basic"));
            result.remove(Mekanism.rl("control_circuit/advanced"));
            result.remove(Mekanism.rl("control_circuit/elite"));
            result.remove(Mekanism.rl("control_circuit/ultimate"));
        } else {
            result.remove(AMEConstants.rl("alloy/infused"));
            result.remove(AMEConstants.rl("alloy/reinforced"));
            result.remove(AMEConstants.rl("alloy/atomic"));
        }
        return result;
    }
}
