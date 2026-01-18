package astral_mekanism.registries;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.resources.ResourceLocation;
import java.util.EnumMap;
import java.util.function.Supplier;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.registration.ExtendedSlurryDeferredRegister;

public class AstralMekanismSlurries {
    public static final ExtendedSlurryDeferredRegister SLURRIES = new ExtendedSlurryDeferredRegister(
            AstralMekanismID.MODID);

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
