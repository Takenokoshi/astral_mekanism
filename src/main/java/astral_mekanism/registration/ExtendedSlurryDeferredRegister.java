package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryBuilder;
import mekanism.common.registration.impl.SlurryDeferredRegister;
import mekanism.common.registration.impl.SlurryRegistryObject;

public class ExtendedSlurryDeferredRegister extends SlurryDeferredRegister {

    private final List<SlurryRegistryObject<?, ?>> allSlurries;

    public ExtendedSlurryDeferredRegister(String modid) {
        super(modid);
        allSlurries = new ArrayList<>();
    }

    @Override
    public SlurryRegistryObject<Slurry, Slurry> register(String baseName,
            UnaryOperator<SlurryBuilder> builderModifier) {
        SlurryRegistryObject<Slurry, Slurry> slurry = super.register(baseName, builderModifier);
        allSlurries.add(slurry);
        return slurry;
    }

    public List<SlurryRegistryObject<?,?>> getAllSlurries(){
        return allSlurries;
    }

}
