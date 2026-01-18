package astral_mekanism.registries;

public enum OreType {
    COAL("coal", false),
    DIAMOND("diamond", false),
    EMERALD("emerald", false),
    FLUORITE("fluorite", false),
    LAPIS_LAZULI("lapis_lazuli", false),
    QUARTZ("quartz", false),
    REDSTONE("redstone", false),
    CERTUS_QUARTZ("certus_quartz", false),
    AMETHYST("amethyst", false),
    IRON("iron", true),
    GOLD("gold", true),
    COPPER("copper", true),
    TIN("tin", true),
    LEAD("lead", true),
    URANIUM("uranium", true),
    OSMIUM("osmium", true),
    NETHERITE("netherite", true),
    NAQUADAH("naquadah", true),;

    private OreType(String type, boolean hasMekprocessing) {
        this.type = type;
        this.hasMekprocessing = hasMekprocessing;
    }

    public final String type;
    public final boolean hasMekprocessing;
}
