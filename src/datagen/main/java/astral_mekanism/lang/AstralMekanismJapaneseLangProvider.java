package astral_mekanism.lang;

import java.util.HashMap;
import java.util.function.Supplier;

import astral_mekanism.AstralMekanismID;
import mekanism.common.util.text.InputValidator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class AstralMekanismJapaneseLangProvider extends LanguageProvider {

    public AstralMekanismJapaneseLangProvider(PackOutput output, String modid, String locale) {
        super(output, AstralMekanismID.MODID, "ja_jp_generated");
    }

    @Override
    protected void addTranslations() {
    }

    private static String toTitle(String path) {
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();

        if (parts[0].equals("crystal") && parts[parts.length - 1].equals("charged")) {
            parts[0] = "チャージ済";
            parts[parts.length - 1] = "の水晶";
        } else if (parts[0].equals("alloy")
                || parts[0].equals("clump")
                || parts[0].equals("crystal")
                || parts[0].equals("shard")
                || parts[0].equals("dust")
                || parts[0].equals("starlight")) {
            String[] neo = new String[parts.length];
            for (int i = 0; i < neo.length - 1; i++) {
                neo[i] = parts[i + 1];
            }
            neo[neo.length - 1] = parts[0];
            parts = neo;
        } else if (parts[0].equals("dirty") && parts[1].equals("dust")) {
            String[] neo = new String[parts.length];
            for (int i = 0; i < neo.length - 2; i++) {
                neo[i] = parts[i + 2];
            }
            neo[neo.length - 2] = parts[0];
            neo[neo.length - 1] = parts[1];
            parts = neo;
        } else if (parts[0].equals("crushed")) {
            String[] neo = new String[parts.length];
            for (int i = 0; i < neo.length - 1; i++) {
                neo[i] = parts[i + 1];
            }
            neo[neo.length - 1] = "debris";
            parts = neo;
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }
            if (map.containsKey(parts[i])) {
                sb.append(map.get(parts[i]));
            } else if (!InputValidator.LETTER.test(parts[i].charAt(0))) {
                sb.append(parts[i]);
            } else {
                sb.append(Character.toUpperCase(parts[i].charAt(0)));
                if (parts[i].length() > 1) {
                    sb.append(parts[i].substring(1));
                }
            }
        }
        return sb.toString();
    }

    private static final HashMap<String, String> map = ((Supplier<HashMap<String, String>>) () -> {
        HashMap<String, String> result = new HashMap<>();
        result.put("utility", "ユーティリティ");
        result.put("block", "ブロック");
        result.put("astral", "アストラル");
        result.put("diamond", "ダイヤモンド");
        result.put("mekanical", "メカニカル");
        result.put("light", "ライト");
        return result;
    }).get();

}
