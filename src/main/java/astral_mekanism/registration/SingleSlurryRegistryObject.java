package astral_mekanism.registration;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.providers.IChemicalProvider;
import mekanism.common.registration.WrappedRegistryObject;
import net.minecraftforge.registries.RegistryObject;

public class SingleSlurryRegistryObject<SLURRY extends Slurry> extends WrappedRegistryObject<SLURRY> implements IChemicalProvider<Slurry> {

    public SingleSlurryRegistryObject(RegistryObject<SLURRY> registryObject) {
        super(registryObject);
    }

    @Override
    public Slurry getChemical() {
        return this.get();
    }

    @Override
    public SlurryStack getStack(long size) {
        return this.get().getStack(size);
    }
    
}
