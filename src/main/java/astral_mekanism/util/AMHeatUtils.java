package astral_mekanism.util;

import mekanism.api.heat.IHeatHandler;

public class AMHeatUtils {

    public static void averagingTemp(IHeatHandler... handlers) {
        final int length = handlers.length;
        if (length < 2) {
            return;
        }
        for (IHeatHandler handler : handlers) {
            if (handler == null || handler.getHeatCapacitorCount() != 1) {
                return;
            }
        }
        double[] c = new double[length];
        double totalc = 0d;
        for (int i = 0; i < length; i++) {
            c[i] = handlers[i].getHeatCapacity(0);
            if (c[i] <= 0) {
                return;
            }
            totalc += c[i];
        }
        double[] t = new double[length];
        double avt = 0d;
        for (int i = 0; i < length; i++) {
            t[i] = handlers[i].getTemperature(0);
            avt += (c[i] / totalc) * t[i];
        }
        double[] d = new double[length];
        boolean cancel = true;
        for (int i = 0; i < length; i++) {
            d[i] = c[i] * (avt - t[i]);
            cancel &= Math.abs(d[i]) < 1e-12;
        }
        if (cancel) {
            return;
        }
        for (int i = 0; i < length; i++) {
            handlers[i].handleHeat(d[i]);
        }
    }
}
