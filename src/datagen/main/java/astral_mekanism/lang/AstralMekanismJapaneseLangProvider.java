package astral_mekanism.lang;

import java.util.HashMap;
import java.util.function.Supplier;

import astral_mekanism.AMEConstants;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismFluids;
import astral_mekanism.registries.AstralMekanismGases;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismSlurries;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IItemProvider;
import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.common.registration.impl.GasRegistryObject;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;
import mekanism.common.registration.impl.SlurryRegistryObject;
import mekanism.common.util.text.InputValidator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.pedroksl.ae2addonlib.registry.helpers.LibBlockDefinition;

public class AstralMekanismJapaneseLangProvider extends LanguageProvider {

    public AstralMekanismJapaneseLangProvider(PackOutput output) {
        super(output, AMEConstants.MODID, "ja_jp_generated");
    }

    @Override
    protected void addTranslations() {
        AstralMekanismMachines.MACHINES.getAllMachines().forEach(this::addMachine);
        AstralMekanismBlocks.BLOCKS.getAllBlocks().forEach(this::addBlock);
        AMEBlockDefinitions.INSTANCE.getBlocks().forEach(this::addBlock);
        AstralMekanismItems.ITEMS.getAllItems().forEach(this::addItem);
        AstralMekanismFluids.FLUIDS.getAllFluids().forEach(this::addFluid);
        AstralMekanismGases.GASES.getAllGases().forEach(this::addGas);
        AstralMekanismInfuseTypes.INFUSE_TYPES.getAllInfuseType().forEach(this::addInfuse);
        AstralMekanismSlurries.SLURRIES.getAllSlurries().forEach(this::addSlurry);
    }

    private void addMachine(MachineRegistryObject<?, ?, ?, ?> machine) {
        String path = machine.getRegistryName().getPath();
        if (path.split("_")[0].equals("astral")) {
            if (path.contains("factory")) {
                path = "factory" + path;
            } else {
                path = "machine" + path;
            }
        } else if (path.endsWith("factory") && path.contains("astral")) {
            path = path.replace("astral", "machineastral");
        }
        addBlock(machine);
        add("container.astral_mekanism." + machine.getRegistryName().getPath(), toTitle(path));
    }

    private void addBlock(IBlockProvider block) {
        String path = block.getRegistryName().getPath();
        if (block instanceof MachineRegistryObject && path.split("_")[0].equals("astral")) {
            if (path.contains("factory")) {
                path = path.replace("astral", "factoryastral");
            } else {
                path = "machine" + path;
            }
        } else if (path.endsWith("factory") && path.contains("astral")) {
            path = path.replace("astral", "machineastral");
        }
        add(block.getBlock(), toTitle(path));
    }

    private void addBlock(LibBlockDefinition<?> definition) {
        add(definition.block(), toTitle(definition.id().getPath()));
    }

    private void addItem(IItemProvider item) {
        String path = item.getRegistryName().getPath();
        if (path.contains("crystal_antimatter")) {
            path = path.replace("crystal_antimatter", "反物質の水晶");
        } else if (path.contains("infuse")) {
            path = path.replace("infuse", "融合");
        }
        add(item.asItem(), toTitle(path));
    }

    private void addFluid(FluidRegistryObject<?, ?, ?, ?, ?> fluid) {
        String path = fluid.getRegistryName().getPath();
        add(fluid.getBucket(), toTitle(path) + "入りバケツ");
        add("fluid.astral_mekanism." + path, toTitle(path));
        add(fluid.getBlock(), toTitle(path));
    }

    private void addGas(GasRegistryObject<?> gas) {
        String path = gas.getRegistryName().getPath();
        add("gas.astral_mekanism." + path, toTitle(path));
    }

    private void addInfuse(InfuseTypeRegistryObject<?> infuse) {
        String path = infuse.getRegistryName().getPath();
        add("infuse_type.astral_mekanism." + path, toTitle(path));
    }

    private void addSlurry(SlurryRegistryObject<?, ?> slurry) {
        add(slurry.getCleanSlurry().getTranslationKey(),
                toTitle(slurry.getCleanSlurry().getRegistryName().getPath()) + "の懸濁液");
        add(slurry.getDirtySlurry().getTranslationKey(),
                toTitle(slurry.getDirtySlurry().getRegistryName().getPath()) + "の懸濁液");
    }

    private static String toTitle(String path) {
        path = path.contains("_compact_") ? "compact_" + path.replace("_compact_", "_") : path;
        path = path.contains("_naquadah_reactor") ? path.replace("_naquadah_reactor", "_ナクアダリアクター") : path;
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        if (parts[0].equals("alloy")
                || parts[0].equals("clump")
                || parts[0].equals("crystal")
                || parts[0].equals("shard")
                || parts[0].equals("dust")
                || parts[0].equals("starlight")
                || parts[0].equals("crushed")
                || parts[0].equals("raw")) {// move first to last
            String moving = parts[0];
            for (int i = 0; i < parts.length - 1; i++) {
                parts[i] = parts[i + 1];
            }
            parts[parts.length - 1] = moving;
        } else if (parts[0].equals("dirty") && parts[1].equals("dust")) {// move second to last
            String moving = parts[1];
            for (int i = 1; i < parts.length - 1; i++) {
                parts[i] = parts[i + 1];
            }
            parts[parts.length - 1] = moving;
        } else if (parts[parts.length - 1].equals("charged")
                || parts[parts.length - 1].equals("fluid")) {// move last to first
            String moving = parts[parts.length - 1];
            for (int i = parts.length - 1; i > 0; i--) {
                parts[i] = parts[i - 1];
            }
            parts[0] = moving;
        } else if (parts[0].equals("printed") && parts[parts.length - 1].equals("processor")) {
            String[] neo = new String[parts.length - 1];
            for (int i = 0; i < neo.length - 1; i++) {
                neo[i] = parts[i + 1];
            }
            neo[neo.length - 1] = "circuit";
            parts = neo;
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }
            if (wordMap.containsKey(parts[i])) {
                sb.append(wordMap.get(parts[i]));
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

    private static final HashMap<String, String> wordMap = ((Supplier<HashMap<String, String>>) () -> {
        HashMap<String, String> result = new HashMap<>();
        // tier
        result.put("essential", "基礎型");
        result.put("basic", "基本");
        result.put("standard", "標準");
        result.put("advanced", "発展");
        result.put("elite", "精鋭");
        result.put("enchanted", "魔性");
        result.put("ultimate", "究極");
        result.put("absolute", "絶対");
        result.put("overclocked", "超速");
        result.put("supreme", "至高");
        result.put("cosmic", "宇宙");
        result.put("dense", "高密");
        result.put("infinite", "無限");
        result.put("multiversal", "多元");
        result.put("machineastral", "天体型");
        result.put("factoryastral", "天体級");
        result.put("astronomical", "天文級");
        result.put("logic", "基本");
        result.put("calculation", "発展");
        result.put("engineering", "上級");
        result.put("accumulation", "精密");
        result.put("photon", "光子");
        result.put("quantum", "量子");
        result.put("composite", "複合");
        result.put("origin", "本質");
        result.put("autonomy", "自律");
        result.put("firmament", "天空");
        // machine types
        result.put("energy", "エナジー");
        result.put("cell", "セル");
        result.put("energized", "電動");
        result.put("smelting", "精錬");
        result.put("smelter", "精錬機");
        result.put("chemical", "化学");
        result.put("injection", "注入");
        result.put("chamber", "室");
        result.put("compressor", "圧縮機");
        result.put("purification", "浄化");
        result.put("crusher", "粉砕機");
        result.put("enrichment", "濃縮");
        result.put("alloyer", "合金化機");
        result.put("apt", "反物質式超分子変換装置");
        result.put("antiprotonic", "反陽子");
        result.put("nucleosynthesizer", "核合成機");
        result.put("infuser", "混合機");
        result.put("oxidizer", "酸化機");
        result.put("washer", "洗浄機");
        result.put("chemixer", "化学混合機");
        result.put("combiner", "結合機");
        result.put("crystallizer", "結晶化装置");
        result.put("dissolution", "溶解");
        result.put("electrolytic", "電解");
        result.put("separator", "分離機");
        result.put("fluid", "液体");
        result.put("formulaic", "定式");
        result.put("assemblicator", "組み立て機");
        result.put("gna", "グロウストーン中性子反応機");
        result.put("greenhouse", "グリーンハウス");
        result.put("isotopic", "同位体");
        result.put("centrifuge", "遠心分離機");
        result.put("charger", "チャージャー");
        result.put("inscriber", "インスクライバー");
        result.put("transformer", "トランスフォーマー");
        result.put("thermalizer", "サーマライザ");
        result.put("metallurgic", "冶金");
        result.put("infuser2", "吹込機");
        result.put("prc", "加圧反応室");
        result.put("precision", "精密");
        result.put("sawmill", "製材機");
        result.put("rotary", "回転式");
        result.put("condensentrator", "流体凝縮機");
        result.put("solidification", "ソリデフィケーション");
        result.put("sps", "超臨界相転移装置");
        result.put("compact", "コンパクトな");
        result.put("fir", "核分裂炉");
        result.put("fusion", "核融合");
        result.put("reactor", "炉");
        result.put("tep", "加温蒸発濃縮プラント");
        result.put("burning", "燃焼");
        result.put("heat", "熱");
        result.put("generator", "発電機");
        result.put("factory", "ファクトリー");
        result.put("crafter", "拡張作業台");
        result.put("synthesizer", "合成装置");
        result.put("neutron", "中性子");
        result.put("activator", "反応機");
        result.put("unzipper", "解凍機");
        result.put("storage", "ストレージ");
        result.put("sortable", "仕分け");
        result.put("tank", "タンク");
        result.put("drive", "MEドライブ");
        // materal types
        result.put("coal", "石炭");
        result.put("diamond", "ダイヤモンド");
        result.put("emerald", "エメラルド");
        result.put("fluorite", "蛍石");
        result.put("lapis", "ラピス");
        result.put("lazuli", "ラズリ");
        result.put("quartz", "クォーツ");
        result.put("redstone", "レッドストーン");
        result.put("certus", "ケルタス");
        result.put("amethyst", "アメジスト");
        result.put("iron", "鉄");
        result.put("gold", "金");
        result.put("copper", "銅");
        result.put("tin", "錫");
        result.put("lead", "鉛");
        result.put("uranium", "ウラン");
        result.put("osmium", "オスミウム");
        result.put("netherite", "ネザライト");
        result.put("naquadah", "ナクアダ");
        result.put("refined", "精製");
        result.put("glowstone", "グロウストーン");
        result.put("alloy", "合金");
        result.put("utility", "ユーティリティ");
        result.put("sodium", "ナトリウム");
        result.put("hydroxide", "水酸化");
        result.put("antimatter", "反物質");
        result.put("biomass", "バイオマス");
        result.put("singularity", "シンギュラリティ");
        result.put("nether", "ネザー");
        result.put("star", "スター");
        result.put("coal", "石炭");
        result.put("elastic", "弾力");
        result.put("convergent", "収束");
        result.put("infuse", "融合");
        result.put("stardust", "星屑");
        result.put("starry", "星");
        result.put("sky", "空");
        result.put("vibration", "振動");
        result.put("resonance", "共振");
        result.put("enhanced", "強化");
        result.put("illusion", "幻想");
        result.put("insert", "搬入");
        result.put("xp", "経験値");
        result.put("lava", "溶岩");
        result.put("netherrack", "ネザーラック");
        result.put("ammonia", "アンモニア");
        result.put("water", "水");
        result.put("nitric", "硝");
        result.put("acid", "酸");
        result.put("aqua", "王");
        result.put("regia", "水");
        result.put("ether", "エーテル");
        result.put("oleum", "発煙硫酸");
        // material states
        result.put("astral", "アストラル");
        result.put("ingot", "インゴット");
        result.put("block", "ブロック");
        result.put("processor", "プロセッサ");
        result.put("press", "の金型");
        result.put("dust", "の粉");
        result.put("golden", "金色の");
        result.put("charged", "チャージ済");
        result.put("cluster", "クラスター");
        result.put("enriched", "濃縮");
        result.put("control", "制御");
        result.put("circuit", "回路");
        result.put("upgrade", "アップグレード");
        result.put("starlight", "のための星の耀き");
        result.put("dirty", "汚れた");
        result.put("compressed", "圧縮された");
        result.put("clump", "の凝塊");
        result.put("crushed", "粉砕された");
        result.put("crystal", "の結晶");
        result.put("raw", "の原石");
        result.put("shard", "の欠片");
        result.put("mekanical", "メカニカル");
        result.put("universal", "ユニバーサル");
        result.put("gas", "ガス");
        result.put("infuse", "インフューズ");
        result.put("red", "赤い");
        result.put("soul", "魂の");
        result.put("mixed", "混合");
        result.put("slurry", "の懸濁液");
        result.put("clean", "きれいな");
        result.put("paste", "ペースト");
        // other
        result.put("item", "アイテム");
        result.put("spacetime", "スペース・タイム");
        result.put("modulation", " モジュレーション");
        result.put("core", " コア");
        result.put("light", "ライト");
        result.put("cable", "ケーブル");
        result.put("bucket", "入りバケツ");
        return result;
    }).get();

}
