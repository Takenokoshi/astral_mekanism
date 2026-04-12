package astral_mekanism.registryenum;

public enum AMEProcessableMaterialType {
    IRON("iron", 0xFFC8C8C8),
    GOLD("gold", 0xFFFFD700),
    COPPER("copper", 0xFFB87333),
    NETHERITE("netherite", 0xFF3C2F3C),
    OSMIUM("osmium", 0xFF6FA8DC),
    TIN("tin", 0xFFD8D8E0),
    LEAD("lead", 0xFF4A4A5A),
    URANIUM("uranium", 0xFF5FFF3A),
    NAQUADAH("naquadah", 0xFF1A3D2F),
    COAL("coal", 0xFF2B2B2B, false, 4),
    DIAMOND("diamond", 0xFF5FE3D9, false),
    EMERALD("emerald", 0xFF00C853, false),
    REDSTONE("redstone", 0xFFFF0000, false, 32),
    LAPIS_LAZULI("lapis_lazuli", 0xFF1E3AA8, false, 64),
    QUARTZ("quartz", 0xFFF5F5F5, false),
    FLUORITE("fluorite", 0xFF7DF9FF, false, 32),
    AMETHYST("amethyst", 0xFF9966CC, false, 64),
    CERTUS_QUARTZ("certus_quartz", 0xFFB9D9FF, false, 64),
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
