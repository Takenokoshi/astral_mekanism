package astral_mekanism.registryenum;

public enum AMEProcessableMaterialType {
    IRON("iron"),
    GOLD("gold"),
    COPPER("copper"),
    NETHERITE("netherite"),
    OSMIUM("osmium"),
    TIN("tin"),
    LEAD("lead"),
    URANIUM("uranium"),
    NAQUADAH("naquadah"),
    COAL("coal", false, 4),
    DIAMOND("diamond", false),
    EMERALD("emerald", false),
    REDSTONE("redstone", false, 32),
    LAPIS_LAZULI("lapis_lazuli", false, 64),
    QUARTZ("quartz", false),
    FLUORITE("fluorite", false, 32),
    AMETHYST("amethyst", false, 64),
    CERTUS_QUARTZ("certus_quartz", false, 64),
    ;

    public final String name;
    public final boolean isMetal;
    public final int additionalMultiply;

    private AMEProcessableMaterialType(String name, boolean isMetal, int additionalMultiply) {
        this.name = name;
        this.isMetal = isMetal;
        this.additionalMultiply = additionalMultiply;
    }

    private AMEProcessableMaterialType(String name, boolean isMetal) {
        this(name, isMetal, 1);
    }

    private AMEProcessableMaterialType(String name) {
        this(name, true);
    }
}
