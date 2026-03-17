package astral_mekanism.registries;

import java.util.function.Supplier;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import astral_mekanism.AMEConstants;
import net.minecraft.world.inventory.MenuType;
import net.pedroksl.ae2addonlib.registry.MenuRegistry;

public class AMEMenus extends MenuRegistry {

    public static final AMEMenus INSTANCE = new AMEMenus();

    public AMEMenus() {
        super(AMEConstants.MODID);
    }
    private static <M extends AEBaseMenu, H> Supplier<MenuType<M>> create(
            String id, MenuTypeBuilder.MenuFactory<M, H> factory, Class<H> host) {
        return create(AMEConstants.MODID, id, factory, host);
    }

    private static <T extends AEBaseMenu> Supplier<MenuType<T>> create(String id, Supplier<MenuType<T>> supplier) {
        return create(AMEConstants.MODID, id, supplier);
    }
}
