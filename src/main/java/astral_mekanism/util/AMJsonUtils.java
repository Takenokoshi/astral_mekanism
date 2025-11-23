package astral_mekanism.util;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.GsonHelper;

public class AMJsonUtils {
    public static JsonElement read(@NotNull JsonObject json, @NotNull String key) {
        return GsonHelper.isArrayNode(json, key)
                ? GsonHelper.getAsJsonArray(json, key)
                : GsonHelper.getAsJsonObject(json, key);
    }
}
