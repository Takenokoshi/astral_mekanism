package astral_mekanism.registration;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

public class ExtendedContainerRegistryObject<CONTAINER extends MekanismTileContainer<? extends TileEntityMekanism>>
        extends ContainerTypeRegistryObject<CONTAINER> {

    public ExtendedContainerRegistryObject(RegistryObject<MenuType<CONTAINER>> registryObject) {
        super(registryObject);
    }

    public ExtendedContainerRegistryObject<CONTAINER> setRegistryObjectEx(
            RegistryObject<MenuType<CONTAINER>> registryObject) {
        this.registryObject = registryObject;
        return this;
    }
}
