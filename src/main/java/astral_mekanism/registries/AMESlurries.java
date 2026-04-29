package astral_mekanism.registries;

import mekanism.api.chemical.slurry.Slurry;
import java.util.EnumMap;
import astral_mekanism.AMEConstants;
import astral_mekanism.registration.ExtendedSlurryDeferredRegister;
import astral_mekanism.registration.SingleSlurryRegistryObject;
import astral_mekanism.registryenum.AMEProcessableMaterialType;

public class AMESlurries {
    public static final ExtendedSlurryDeferredRegister SLURRIES = new ExtendedSlurryDeferredRegister(
            AMEConstants.MODID);

    public static final EnumMap<AMEProcessableMaterialType, SingleSlurryRegistryObject<Slurry>> SPECIFIC_SLURRIES = createUniqueSlurries(
            "specific");
    public static final EnumMap<AMEProcessableMaterialType, SingleSlurryRegistryObject<Slurry>> SHINING_SLURRIES = createUniqueSlurries(
            "shining");

    private static EnumMap<AMEProcessableMaterialType, SingleSlurryRegistryObject<Slurry>> createUniqueSlurries(
            String additionalName) {
        EnumMap<AMEProcessableMaterialType, SingleSlurryRegistryObject<Slurry>> result = new EnumMap<>(
                AMEProcessableMaterialType.class);
        for (AMEProcessableMaterialType type : AMEProcessableMaterialType.values()) {
            result.put(type, SLURRIES.registerSingle(additionalName + "_" + type.name + "_slurry",
                    builder -> builder.ore(AMEConstants.rl("feedstocks/" + type.name)).tint(type.tint)));
        }
        return result;
    }
};
