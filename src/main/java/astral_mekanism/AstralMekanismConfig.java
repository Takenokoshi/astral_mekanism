package astral_mekanism;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AstralMekanism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralMekanismConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
			.comment("Whether to log the dirt block on common setup")
			.define("logDirtBlock", true);

	private static final ForgeConfigSpec.IntValue ENERGY_RATE = BUILDER
			.comment("Machine energy using rate")
			.defineInRange("value", 1, 0, Integer.MAX_VALUE);

	public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
			.comment("What you want the introduction message to be for the magic number")
			.define("magicNumberIntroduction", "The magic number is... ");

	private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
			.comment("A list of items to log on common setup.")
			.defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), AstralMekanismConfig::validateItemName);

	static final ForgeConfigSpec SPEC = BUILDER.build();

	public static boolean logDirtBlock;
	public static int energyRate;
	public static String magicNumberIntroduction;
	public static Set<Item> items;

	private static boolean validateItemName(final Object obj) {
		return obj instanceof final String itemName
				&& ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		logDirtBlock = LOG_DIRT_BLOCK.get();
		energyRate = ENERGY_RATE.get();
		magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

		// convert the list of strings into a set of items
		items = ITEM_STRINGS.get().stream()
				.map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
				.collect(Collectors.toSet());
	}
}
