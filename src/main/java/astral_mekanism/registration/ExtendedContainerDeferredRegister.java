package astral_mekanism.registration;

import java.util.function.Supplier;

import astral_mekanism.registration.RegistrationInterfaces.ContainerConstructor;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.container.type.MekanismContainerType;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.tile.base.TileEntityMekanism;

public class ExtendedContainerDeferredRegister extends ContainerTypeDeferredRegister {

    public ExtendedContainerDeferredRegister(String modid) {
        super(modid);
    }

    public <TILE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<TILE>> ExtendedContainerRegistryObject<CONTAINER> register(
            String name, Class<TILE> tileClass, ContainerConstructor<TILE, CONTAINER> constructor) {
        ExtendedContainerRegistryObject<CONTAINER> object = new ExtendedContainerRegistryObject<>(null);
        MekanismContainerType.IMekanismContainerFactory<TILE, CONTAINER> factory = (id, inv,
                data) -> constructor
                        .create(object, id, inv, data);
        Supplier<MekanismContainerType<TILE, CONTAINER>> supplier = () -> MekanismContainerType.tile(tileClass,
                factory);
        return this.register(name, supplier, ro -> object.setRegistryObjectEx(ro));
    }

}
