package astral_mekanism.registryenum;

public enum AMEProcessableMaterialType {
    IRON("iron", 0xC8C8C8),
    GOLD("gold", 0xFFD700),
    COPPER("copper", 0xB87333),
    NETHERITE("netherite", 0x3C2F3C),
    OSMIUM("osmium", 0x6FA8DC),
    TIN("tin", 0xD8D8E0),
    LEAD("lead", 0x4A4A5A),
    URANIUM("uranium", 0x5FFF3A),
    NAQUADAH("naquadah", 0x1A3D2F),
    COAL("coal", 0x2B2B2B, false, 4),
    DIAMOND("diamond", 0x5FE3D9, false),
    EMERALD("emerald", 0x00C853, false),
    REDSTONE("redstone", 0xFF0000, false, 32),
    LAPIS_LAZULI("lapis_lazuli", 0x1E3AA8, false, 64),
    QUARTZ("quartz", 0xF5F5F5, false),
    FLUORITE("fluorite", 0x7DF9FF, false, 32),
    AMETHYST("amethyst", 0x9966CC, false, 64),
    CERTUS_QUARTZ("certus_quartz", 0xB9D9FF, false, 64),
    ;

    public final String name;
    public final int tint;
    public final boolean isMetal;
    public final int additionalMultiply;

    private AMEProcessableMaterialType(String name, int tint, boolean isMetal, int additionalMultiply) {
        this.name = name;
        this.tint = tint;
        this.isMetal = isMetal;
        this.additionalMultiply = additionalMultiply;
    }

    private AMEProcessableMaterialType(String name, int tint, boolean isMetal) {
        this(name, tint, isMetal, 1);
    }

    private AMEProcessableMaterialType(String name, int tint) {
        this(name, tint, true);
    }
}
