package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import mekanism.api.chemical.attribute.ChemicalAttribute;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.base.IChemicalConstant;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class ExtendedGasDeferredRegister extends GasDeferredRegister {

    private final List<GasRegistryObject<?>> allGases;

    public ExtendedGasDeferredRegister(String modid) {
        super(modid);
        this.allGases = new ArrayList<>();
    }

    @Override
    public GasRegistryObject<Gas> register(IChemicalConstant constants, ChemicalAttribute... attributes) {
        GasRegistryObject<Gas> gas = super.register(constants, attributes);
        allGases.add(gas);
        return gas;
    }

    @Override
    public GasRegistryObject<Gas> register(String name, int color, ChemicalAttribute... attributes) {
        GasRegistryObject<Gas> gas = super.register(name, color, attributes);
        allGases.add(gas);
        return gas;
    }

    @Override
    public <GAS extends Gas> GasRegistryObject<GAS> register(String name, Supplier<? extends GAS> sup) {
        GasRegistryObject<GAS> gas = super.register(name, sup);
        allGases.add(gas);
        return gas;
    }

    public List<GasRegistryObject<?>> getAllGases(){
        return allGases;
    }

}
