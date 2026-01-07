package astral_mekanism.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import mekanism.api.chemical.infuse.InfuseType;
import mekanism.common.registration.impl.InfuseTypeDeferredRegister;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;

public class ExtendedInfuseTypeDeferredRegister extends InfuseTypeDeferredRegister {

    private final List<InfuseTypeRegistryObject<?>> allInfuseTypes;

    public ExtendedInfuseTypeDeferredRegister(String modid) {
        super(modid);
        allInfuseTypes = new ArrayList<>();
    }

    @Override
    public <INFUSE_TYPE extends InfuseType> InfuseTypeRegistryObject<INFUSE_TYPE> register(String name,
            Supplier<? extends INFUSE_TYPE> sup) {
        InfuseTypeRegistryObject<INFUSE_TYPE> infuse = super.register(name, sup);
        allInfuseTypes.add(infuse);
        return infuse;
    }

    public List<InfuseTypeRegistryObject<?>> getAllInfuseType() {
        return allInfuseTypes;
    }

}
