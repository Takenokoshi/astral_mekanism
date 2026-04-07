package astral_mekanism.registries;

import astral_mekanism.AMEConstants;
import net.pedroksl.ae2addonlib.registry.ItemRegistry;

//Requierd by AMEBlockDefinitions.
public final class AMEItemDefinitions extends ItemRegistry {

    public static final AMEItemDefinitions INSTANCE = new AMEItemDefinitions();

    public AMEItemDefinitions() {
        super(AMEConstants.MODID);
    }

}
