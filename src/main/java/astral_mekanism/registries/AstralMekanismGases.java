package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class AstralMekanismGases {
	public static final GasDeferredRegister GASES = new GasDeferredRegister(AstralMekanismID.MODID);
	static final GasRegistryObject<Gas> UTILITY_GAS = GASES.register("utility_gas", 0xFF55FF);
	static final GasRegistryObject<Gas> AMMONIA = GASES.register("ammonia", 0x6699FF);
	static final GasRegistryObject<Gas> NITRIC_ACID = GASES.register("nitric_acid", 0xFFF5CC);
	static final GasRegistryObject<Gas> AQUA_REGIA = GASES.register("aqua_regia", 0xFF6600);
	static final GasRegistryObject<Gas> ASTRAL_ETHER = GASES.register("astral_ether", 0xD4A1FF);
	static final GasRegistryObject<Gas> DILUTED_ASTRAL_ETHER = GASES.register("diluted_astral_ether", 0xF4C1FF);
}
