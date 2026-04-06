package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryBuilder;
import mekanism.common.registration.impl.SlurryDeferredRegister;
import mekanism.common.registration.impl.SlurryRegistryObject;

public class ExtendedSlurryDeferredRegister extends SlurryDeferredRegister {

    private final List<SlurryRegistryObject<?, ?>> allSlurries;
    private final List<SingleSlurryRegistryObject<?>> allSingleSlurries;

    public ExtendedSlurryDeferredRegister(String modid) {
        super(modid);
        allSlurries = new ArrayList<>();
        allSingleSlurries = new ArrayList<>();
    }

    @Override
    public SlurryRegistryObject<Slurry, Slurry> register(String baseName,
            UnaryOperator<SlurryBuilder> builderModifier) {
        SlurryRegistryObject<Slurry, Slurry> slurry = super.register(baseName, builderModifier);
        allSlurries.add(slurry);
        return slurry;
    }

    public <SLURRY extends Slurry> SingleSlurryRegistryObject<SLURRY> registerSingle(String name,
            Function<SlurryBuilder, SLURRY> creator,
            UnaryOperator<SlurryBuilder> builderModifier) {
        SingleSlurryRegistryObject<SLURRY> slurry = new SingleSlurryRegistryObject<>(
                internal.register(name, () -> creator.apply(SlurryBuilder.clean())));
        allSingleSlurries.add(slurry);
        return slurry;
    }

    public SingleSlurryRegistryObject<Slurry> registerSingle(String name,
            UnaryOperator<SlurryBuilder> builderModifier) {
        return registerSingle(name, Slurry::new, builderModifier);
    }

    public List<SlurryRegistryObject<?, ?>> getAllSlurries() {
        return Collections.unmodifiableList(allSlurries);
    }

    public List<SingleSlurryRegistryObject<?>> getAllSingleSlurries() {
        return Collections.unmodifiableList(allSingleSlurries);
    }

}
