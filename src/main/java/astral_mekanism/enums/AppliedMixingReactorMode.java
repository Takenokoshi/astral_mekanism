package astral_mekanism.enums;

import astral_mekanism.AMELang;
import mekanism.api.text.ILangEntry;

public enum AppliedMixingReactorMode {

    MIXED_ONLY(AMELang.ENUM_AMR_MIXED_ONLY),
    MIXED_PRIORITIZE(AMELang.ENUM_AMR_MIXED_PRIORITIZE),
    UNMIXED_PRIORITIZE(AMELang.ENUM_AMR_UNMIXED_PRIORITIZE),
    UNMIXED_ONLY(AMELang.ENUM_AMR_UNMIXED_ONLY),
    ;

    public static final AppliedMixingReactorMode[] MODES = values();
    public final ILangEntry langEntry;

    private AppliedMixingReactorMode(ILangEntry langEntry) {
        this.langEntry = langEntry;
    }

    public AppliedMixingReactorMode getNext() {
        return MODES[(ordinal() + 1) % MODES.length];
    }

    public AppliedMixingReactorMode getPrev() {
        return MODES[(ordinal() - 1) % MODES.length];
    }

    public static AppliedMixingReactorMode byIndex(int i){
        return MODES[i];
    }
}
