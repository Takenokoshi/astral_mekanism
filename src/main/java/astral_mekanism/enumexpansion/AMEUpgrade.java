package astral_mekanism.enumexpansion;

import java.util.EnumMap;
import java.util.Map;

import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public enum AMEUpgrade {
    COBBLESTONE_SUPPLY,
    WATER_SUPPLY,
    XP,
    ;

    private static final EnumMap<Upgrade, AMEUpgrade> map = new EnumMap<>(Upgrade.class);
    private static final String AME_UPGRADE_NBT = "ame_upgrades";
    private static final AMEUpgrade[] AME_UPGRADES = values();

    private Upgrade upgrade;

    private AMEUpgrade() {
        this.upgrade = null;
    }

    public void setValue(Upgrade upgrade) {
        if (this.upgrade == null) {
            this.upgrade = upgrade;
            map.put(upgrade, this);
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
    public static Map<Upgrade, Integer> saveMap(Map<Upgrade, Integer> upgrades, CompoundTag nbtTags) {
        ListTag list = new ListTag();
        for (Upgrade u : Upgrade.values()) {
            if (isAMEUpgrade(u) && upgrades.containsKey(u)) {
                list.add(getAMEUpgrade(u).getTag(upgrades.get(u)));
                upgrades.remove(u);
            }
        }
        nbtTags.put(AME_UPGRADE_NBT, list);
        return upgrades;
    }

    // be called from mixin to read separated savedata nbt of AME's Upgrade.
    public static Map<Upgrade, Integer> buildMap(Map<Upgrade, Integer> upgrades, CompoundTag nbtTags) {
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
