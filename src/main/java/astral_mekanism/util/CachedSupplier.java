package astral_mekanism.util;

import java.util.function.Supplier;

public class CachedSupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private T value;

    public CachedSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

}
