package astral_mekanism.util;

import mekanism.api.heat.IHeatHandler;

public class AMHeatUtils {

    public static void averagingTemp(IHeatHandler a, IHeatHandler b) {
        if (a == null || b == null || a == b || a.getHeatCapacitorCount() != 1 || b.getHeatCapacitorCount() != 1) {
            return;
        }
        double c1 = a.getHeatCapacity(0);
        double c2 = b.getHeatCapacity(0);
        if (c1 <= 0 || c2 <= 0) {
            return;
        }
        double t1 = a.getTemperature(0);
        double t2 = b.getTemperature(0);
        double te = t1 * c1 / (c1 + c2) + t2 * c2 / (c1 + c2);
        double dq1 = c1 * (te - t1);
        double dq2 = -dq1;
        if (Math.abs(dq1) < 1e-12 && Math.abs(dq2) < 1e-12) {
            return;
        }
        a.handleHeat(dq1);
        b.handleHeat(dq2);
    }
}
