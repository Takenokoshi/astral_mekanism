package astral_mekanism.botanypots;

import java.util.Random;

public class FakeRandom extends Random {
    public FakeRandom() {
    }

    @Override
    public float nextFloat() {
        return Float.MIN_VALUE;
    }
}
