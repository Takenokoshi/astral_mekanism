package astral_mekanism.registries;

import java.util.function.Supplier;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier.AstralMekanismTierMap;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities;
import astral_mekanism.block.container.storage.TieredDriveMenu;
import net.minecraft.world.inventory.MenuType;
import net.pedroksl.ae2addonlib.registry.MenuRegistry;

public class AstralMekanismMenus extends MenuRegistry {

    public static final AstralMekanismMenus INSTANCE = new AstralMekanismMenus();

    public AstralMekanismMenus() {
        super(AstralMekanismID.MODID);
    }

    public static final AstralMekanismTierMap<Supplier<MenuType<TieredDriveMenu>>> DRIVES = new AstralMekanismTierMap<>(
            tier -> create(tier.nameForAE + "_drive", TieredDriveMenu::new,
                    TieredDriveBlockEntities.TieredDriveBlockEntity.class));

    private static <M extends AEBaseMenu, H> Supplier<MenuType<M>> create(
            String id, MenuTypeBuilder.MenuFactory<M, H> factory, Class<H> host) {
        return create(AstralMekanismID.MODID, id, factory, host);
    }

    private static <T extends AEBaseMenu> Supplier<MenuType<T>> create(String id, Supplier<MenuType<T>> supplier) {
        return create(AstralMekanismID.MODID, id, supplier);
    }

}
