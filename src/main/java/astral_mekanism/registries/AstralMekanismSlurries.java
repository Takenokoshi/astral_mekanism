package astral_mekanism.registries;

import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.registration.impl.SlurryDeferredRegister;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

import astral_mekanism.AstralMekanismID;

public class AstralMekanismSlurries {
	public static final SlurryDeferredRegister SLURRIES = new SlurryDeferredRegister(
			AstralMekanismID.MODID);

	static final String[] Types = { "coal", "diamond", "emerald", "fluorite", "lapis_lazuli", "quartz",
			"redstone" };

	static List<SlurryRegistryObject<Slurry, Slurry>> new_slurry_register() {
		List<SlurryRegistryObject<Slurry, Slurry>> result = new ArrayList<SlurryRegistryObject<Slurry, Slurry>>();
		for (String type : Types) {
			final SlurryRegistryObject<Slurry, Slurry> S = SLURRIES
					.register(type, builder -> {
						builder.ore(new ResourceLocation("forge", "ores/" + type));
						return builder;
					});
			result.add(S);
		}
		return result;
	};

	static final List<SlurryRegistryObject<Slurry, Slurry>> slurries = new_slurry_register();

};
