package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.common.registration.impl.InfuseTypeDeferredRegister;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;

public class AstralMekanismInfuseTypes {

	public static final InfuseTypeDeferredRegister INFUSE_TYPES = new InfuseTypeDeferredRegister(
			AstralMekanismID.MODID);

	public static final InfuseTypeRegistryObject<InfuseType> UTILITY_INFUSE = INFUSE_TYPES
			.register("utility_infuse", 0xA54080);
}
