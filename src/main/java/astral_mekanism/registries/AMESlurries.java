package astral_mekanism.registries;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.resources.ResourceLocation;
import java.util.EnumMap;
import java.util.function.Supplier;

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

    public static final EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>> GEM_SLURRIES = ((Supplier<EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>>>) (() -> {
        EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>> result = new EnumMap<>(OreType.class);
        for (OreType type : OreType.values()) {
            if (!type.hasMekprocessing || type == OreType.NETHERITE) {
                result.put(type, SLURRIES.register(type.type,
                        builder -> builder.ore(new ResourceLocation("forge", "ores/" + type.type))));
            }
        }
        return result;
    })).get();

    public static final EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>> COMPRESSED_SLURRIES = ((Supplier<EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>>>) (() -> {
        EnumMap<OreType, SlurryRegistryObject<Slurry, Slurry>> result = new EnumMap<>(OreType.class);
        for (OreType type : OreType.values()) {
            result.put(type, SLURRIES.register("compressed_" + type.type,
                    builder -> builder.ore(new ResourceLocation("forge", "ores/" + type.type))));
        }
        return result;
    })).get();

};
