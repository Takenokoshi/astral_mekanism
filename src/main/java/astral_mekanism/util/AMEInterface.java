package astral_mekanism.util;

public class AMEInterface {
    @FunctionalInterface
    public static interface QuadPredicate<P, Q, R, S> {
        public boolean test(P p, Q q, R r, S s);
    }

    @FunctionalInterface
    public static interface PentaPredicate<P, Q, R, S, T> {
        public boolean test(P p, Q q, R r, S s, T t);
    }
}
