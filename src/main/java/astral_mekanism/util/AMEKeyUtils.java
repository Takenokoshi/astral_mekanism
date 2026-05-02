package astral_mekanism.util;

import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;

public class AMEKeyUtils {
    public static GasStack getGas(MekanismKey key) {
        return key.getForm() == MekanismKey.GAS && key.getStack() instanceof GasStack stack
                ? stack
                : GasStack.EMPTY;
    }

    public static InfusionStack getInfusion(MekanismKey key) {
        return key.getForm() == MekanismKey.INFUSION && key.getStack() instanceof InfusionStack stack
                ? stack
                : InfusionStack.EMPTY;
    }

    public static PigmentStack getPigment(MekanismKey key) {
        return key.getForm() == MekanismKey.PIGMENT && key.getStack() instanceof PigmentStack stack
                ? stack
                : PigmentStack.EMPTY;
    }

    public static SlurryStack getSlurry(MekanismKey key) {
        return key.getForm() == MekanismKey.SLURRY && key.getStack() instanceof SlurryStack stack
                ? stack
                : SlurryStack.EMPTY;
    }
}
