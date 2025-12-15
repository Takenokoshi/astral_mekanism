package astral_mekanism.util;

public class AMInterface {
    @FunctionalInterface
    public static interface QuadPredicate<P, Q, R, S> {
        public boolean test(P p, Q q, R r, S s);
    }
}
