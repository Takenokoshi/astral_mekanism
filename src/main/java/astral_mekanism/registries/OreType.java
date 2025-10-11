package astral_mekanism.registries;

public enum OreType {
	COAL("coal", 0x0f0f0f, false),
	DIAMOND("diamond", 0x55aaff, false),
	EMERALD("emerald", 0x55ff99, false),
	FLUORITE("fluorite", 0xaabbff, false),
	LAPIS_LAZULI("lapis_lazuli", 0x4444ff, false),
	QUARTZ("quartz", 0xffffff, false),
	REDSTONE("redstone", 0xff5555, false),
	IRON("iron", 0xC0A7A7, true),
	GOLD("gold", 0xAA6633, true),
	COPPER("copper", 0xCC8822, true),
	TIN("tin", 0xCCCCCC, true),
	LEAD("lead", 0x333333, true),
	URANIUM("uranium", 0x33DD99, true),
	OSMIUM("osmium", 0x4444FF, true),;

	private OreType(String type, int color, boolean hasMekprocessing) {
		this.type = type;
		this.color = color;
		this.hasMekprocessing = hasMekprocessing;
	}

	public final String type;
	public final int color;
	public final boolean hasMekprocessing;
}
