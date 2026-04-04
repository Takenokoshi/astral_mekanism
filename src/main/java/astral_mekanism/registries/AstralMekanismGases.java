package astral_mekanism.registries;

import astral_mekanism.AMEConstants;
import astral_mekanism.registration.ExtendedGasDeferredRegister;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasBuilder;
import mekanism.api.chemical.gas.attribute.GasAttributes.Radiation;
import mekanism.common.registration.impl.GasRegistryObject;
import net.minecraft.resources.ResourceLocation;

public class AstralMekanismGases {
    public static final ExtendedGasDeferredRegister GASES = new ExtendedGasDeferredRegister(AMEConstants.MODID);
    public static final GasRegistryObject<Gas> UTILITY_GAS = GASES.register("utility_gas", 0xFF55FF);
    public static final GasRegistryObject<Gas> POLONIUM_CONTAINING_UTILITY_GAS = GASES
            .register("polonium_containing_utility_gas", 0x8D7ABD);
    public static final GasRegistryObject<Gas> AMMONIA = GASES.register("ammonia", 0x6699FF);
    public static final GasRegistryObject<Gas> NITRIC_ACID = GASES.register("nitric_acid", 0xFFF5CC);
    public static final GasRegistryObject<Gas> AQUA_REGIA = GASES.register("aqua_regia", 0xFF6600, new Radiation(0.01));
    public static final GasRegistryObject<Gas> ASTRAL_ETHER = GASES.register("astral_ether", 0xD4A1FF);
    public static final GasRegistryObject<Gas> NETHERRACK = GASES.register("netherrack", () -> {
        return new Gas(GasBuilder.builder(new ResourceLocation("block/netherrack")));
    });
    public static final GasRegistryObject<Gas> NETHERITE_ETHER = GASES.register("netherite_ether", 0x4A3A33);
    public static final GasRegistryObject<Gas> OLEUM = GASES.register("oleum", 0xE6F2B3, new Radiation(0.01));
    public static final GasRegistryObject<Gas> AIR = GASES.register("air", 0xffffff);
    public static final GasRegistryObject<Gas> SINGULARITY_ACID = GASES.register("singularity_acid", 0x1800a8);
}
