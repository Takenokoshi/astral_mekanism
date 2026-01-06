package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.registration.ExtendedGasDeferredRegister;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasBuilder;
import mekanism.common.registration.impl.GasRegistryObject;
import net.minecraft.resources.ResourceLocation;

public class AstralMekanismGases {
    public static final ExtendedGasDeferredRegister GASES = new ExtendedGasDeferredRegister(AstralMekanismID.MODID);
    public static final GasRegistryObject<Gas> UTILITY_GAS = GASES.register("utility_gas", 0xFF55FF);
    public static final GasRegistryObject<Gas> AMMONIA = GASES.register("ammonia", 0x6699FF);
    public static final GasRegistryObject<Gas> NITRIC_ACID = GASES.register("nitric_acid", 0xFFF5CC);
    public static final GasRegistryObject<Gas> AQUA_REGIA = GASES.register("aqua_regia", 0xFF6600);
    public static final GasRegistryObject<Gas> ASTRAL_ETHER = GASES.register("astral_ether", 0xD4A1FF);
    public static final GasRegistryObject<Gas> DILUTED_ASTRAL_ETHER = GASES.register("diluted_astral_ether", 0xF4C1FF);
    public static final GasRegistryObject<Gas> NETHERRACK = GASES.register("netherrack", () -> {
        return new Gas(GasBuilder.builder(new ResourceLocation("block/netherrack")));
    });
    public static final GasRegistryObject<Gas> NETHERITE_ETHER = GASES.register("netherite_ether", 0x4A3A33);
}
