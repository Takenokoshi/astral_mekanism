package astral_mekanism.enumexpansion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public enum AMEUpgrade {
    COBBLESTONE_SUPPLY,
    WATER_SUPPLY,
    XP,
    RADIOACTIVE_SEALING,
    AIR_INTAKE
    ;

    private static final Map<Upgrade, AMEUpgrade> map = new HashMap<>();// don't use EnumMap.
    public static final String AME_UPGRADE_NBT = "ame_upgrades";
    private static final AMEUpgrade[] AME_UPGRADES = values();

    private Upgrade upgrade;

    private AMEUpgrade() {
        this.upgrade = null;
    }

    public void setValue(Upgrade upgrade) {
        if (this.upgrade == null) {
            this.upgrade = upgrade;
        }
    }

    public static void initializeMap() {
        for (AMEUpgrade ameUpgrade : values()) {
            map.put(ameUpgrade.upgrade, ameUpgrade);
        }
    }

    public Upgrade getValue() {
        return this.upgrade;
    }

    public static AMEUpgrade getAMEUpgrade(Upgrade upgrade) {
        if (map.containsKey(upgrade)) {
            return map.get(upgrade);
        }
        return null;
    }

    public static boolean isAMEUpgrade(Upgrade upgrade) {
        return map.containsKey(upgrade);
    }

    // be called from mixin to separate NBT saving of AME's Upgrade.
    @SuppressWarnings("unchecked")
    public static Set<Map.Entry<Upgrade, Integer>> saveMap(Set<Map.Entry<Upgrade, Integer>> upgrades,
            CompoundTag nbtTags) {
        ListTag list = new ListTag();
        for (Map.Entry<Upgrade, Integer> entry : upgrades) {
            if (AMEUpgrade.isAMEUpgrade(entry.getKey())) {
                list.add(AMEUpgrade.getAMEUpgrade(entry.getKey()).getTag(entry.getValue()));
            }
        }
        nbtTags.put(AME_UPGRADE_NBT, list);
        return Set.of(upgrades.stream()
                .filter(entry -> !AMEUpgrade.isAMEUpgrade(entry.getKey()))
                .toArray(Map.Entry[]::new));
    }

    // be called from mixin to read separated savedata nbt of AME's Upgrade.
    public static Map<Upgrade, Integer> buildMap(Map<Upgrade, Integer> upgrades, CompoundTag nbtTags) {
        if (upgrades == null) {
            upgrades = new HashMap<>();
        }
        if (nbtTags.contains(AME_UPGRADE_NBT)) {
            ListTag list = nbtTags.getList(AME_UPGRADE_NBT, 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                AMEUpgrade ameUpgrade = valueOf(tag.getString(NBTConstants.TYPE));
                int amount = tag.getInt(NBTConstants.AMOUNT);
                upgrades.put(ameUpgrade.upgrade, amount);
            }
        }
        return upgrades;
    }

    public CompoundTag getTag(int amount) {
        CompoundTag result = new CompoundTag();
        result.putString(NBTConstants.TYPE, toString());
        result.putInt(NBTConstants.AMOUNT, amount);
        return result;
    }
}
