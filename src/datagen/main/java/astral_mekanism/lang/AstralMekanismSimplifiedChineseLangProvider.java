package astral_mekanism.lang;

import java.util.HashMap;
import java.util.function.Supplier;

import astral_mekanism.AMEConstants;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.SingleSlurryRegistryObject;
import astral_mekanism.registries.*;
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

public class AstralMekanismSimplifiedChineseLangProvider extends LanguageProvider {

    public AstralMekanismSimplifiedChineseLangProvider(PackOutput output) {
        super(output, AMEConstants.MODID, "zh_cn_generated");
    }

    @Override
    protected void addTranslations() {
        AMEMachines.MACHINES.getAllMachines().forEach(this::addMachine);
        AMEBlocks.BLOCKS.getAllBlocks().forEach(this::addBlock);
        AMEBlockDefinitions.INSTANCE.getBlocks().forEach(this::addBlock);
        AMEItems.ITEMS.getAllItems().forEach(this::addItem);
        AMEFluids.FLUIDS.getAllFluids().forEach(this::addFluid);
        AMEGases.GASES.getAllGases().forEach(this::addGas);
        AMEInfuseTypes.INFUSE_TYPES.getAllInfuseType().forEach(this::addInfuse);
        AMESlurries.SLURRIES.getAllSlurries().forEach(this::addSlurry);
        AMESlurries.SLURRIES.getAllSingleSlurries().forEach(this::addSlurry);
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
            path = path.replace("crystal_antimatter", "反物质结晶");
        } else if (path.contains("infuse")) {
            path = path.replace("infuse", "灌注");
        }
        add(item.asItem(), toTitle(path));
    }

    private void addFluid(FluidRegistryObject<?, ?, ?, ?, ?> fluid) {
        String path = fluid.getRegistryName().getPath();
        add(fluid.getBucket(), toTitle(path) + "桶");
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
                toTitle(slurry.getCleanSlurry().getRegistryName().getPath()) + "纯净浆液");
        add(slurry.getDirtySlurry().getTranslationKey(),
                toTitle(slurry.getDirtySlurry().getRegistryName().getPath()) + "污浊浆液");
    }

    private void addSlurry(SingleSlurryRegistryObject<?> slurry) {
        add(slurry.getTranslationKey(), toTitle(slurry.getRegistryName().getPath()));
    }

    private static String toTitle(String path) {
        path = path.contains("_compact_") ? "compact_" + path.replace("_compact_", "_") : path;
        path = path.contains("_naquadah_reactor") ? path.replace("_naquadah_reactor", "硅岩反应堆") : path;
        path = path.contains("metallurgic_infuser") ? path.replace("metallurgic_infuser", "冶金灌注机") : path;
        path = path.contains("sodium_hydroxide") ? path.replace("sodium_hydroxide", "氢氧化钠") : path;
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        if (parts[0].equals("alloy")
                || parts[0].equals("clump")
                || parts[0].equals("crystal")
                || parts[0].equals("shard")
                || parts[0].equals("dust")
                || parts[0].equals("starlight")
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
        result.put("essential", "基元");
        result.put("basic", "基础");
        result.put("standard", "标准");
        result.put("advanced", "高级");
        result.put("elite", "精英");
        result.put("enchanted", "附魔");
        result.put("ultimate", "终极");
        result.put("absolute", "绝对");
        result.put("overclocked", "超频");
        result.put("supreme", "至尊");
        result.put("cosmic", "寰宇");
        result.put("dense", "致密");
        result.put("infinite", "无限");
        result.put("multiversal", "多元");
        result.put("machineastral", "星界");
        result.put("factoryastral", "星界");
        result.put("astronomical", "超现实");
        result.put("logic", "逻辑");
        result.put("calculation", "计算");
        result.put("engineering", "工程");
        result.put("accumulation", "集群");
        result.put("photon", "光子");
        result.put("quantum", "量子");
        result.put("composite", "复合");
        result.put("origin", "本质");
        result.put("autonomy", "自主");
        result.put("firmament", "苍穹");
        // machine types
        result.put("energy", "能量");
        result.put("cell", "单元");
        result.put("energized", "电力");
        result.put("smelting", "精炼");
        result.put("smelter", "精炼机");
        result.put("chemical", "化学");
        result.put("injection", "注入");
        result.put("chamber", "室");
        result.put("compressor", "压缩机");
        result.put("purification", "净化");
        result.put("crusher", "粉碎机");
        result.put("enrichment", "富集");
        result.put("alloyer", "合金炉");
        result.put("apt", "反物质原分子嬗变仪");
        result.put("antiprotonic", "反质子");
        result.put("nucleosynthesizer", "核合成器");
        result.put("infuser", "混合机");
        result.put("oxidizer", "酸化机");
        result.put("washer", "洗涤器");
        result.put("chemixer", "化学混合机");
        result.put("combiner", "融合机");
        result.put("crystallizer", "结晶化装置");
        result.put("dissolution", "溶解");
        result.put("electrolytic", "电解");
        result.put("separator", "分离机");
        result.put("fluid", "液体");
        result.put("formulaic", "公式");
        result.put("assemblicator", "装配器");
        result.put("gna", "荧石中子活化器");
        result.put("greenhouse", "温室");
        result.put("isotopic", "同位素");
        result.put("centrifuge", "离心机");
        result.put("charger", "充能器");
        result.put("inscriber", "压印器");
        result.put("transformer", "转化合成器");
        result.put("thermalizer", "热熔器");
        result.put("metallurgic", "冶金");
        result.put("infuser2", "灌注器");
        result.put("prc", "加压反应室");
        result.put("precision", "精密");
        result.put("sawmill", "锯木机");
        result.put("rotary", "回旋式");
        result.put("condensentrator", "流体冷凝机");
        result.put("solidification", "固化室");
        result.put("sps", "超临界移相器");
        result.put("compact", "紧凑型");
        result.put("fir", "裂变");
        result.put("fusion", "聚变");
        result.put("reactor", "反应堆");
        result.put("tep", "热力蒸馏厂");
        result.put("burning", "燃烧");
        result.put("heat", "热");
        result.put("generator", "发电机");
        result.put("factory", "工厂");
        result.put("crafter", "扩展工作台");
        result.put("synthesizer", "合成器");
        result.put("neutron", "中子");
        result.put("activator", "反应器");
        result.put("unzipper", "解压机");
        result.put("storage", "存储器");
        result.put("sortable", "分类");
        result.put("tank", "储罐");
        result.put("drive", "ME驱动器");
        result.put("green", "温");
        result.put("house", "室");
        result.put("composter", "堆肥桶");
        result.put("matter", "物质");
        result.put("condenser", "冷凝器");
        result.put("irradiator", "辐照器");
        // materal types
        result.put("coal", "煤炭");
        result.put("diamond", "钻石");
        result.put("emerald", "绿宝石");
        result.put("fluorite", "氟石");
        result.put("lapis", "青金石");
        result.put("lazuli", "天青石");
        result.put("quartz", "石英");
        result.put("redstone", "红石");
        result.put("certus", "赛特斯");
        result.put("amethyst", "紫水晶");
        result.put("iron", "铁");
        result.put("gold", "金");
        result.put("copper", "铜");
        result.put("tin", "锡");
        result.put("lead", "铅");
        result.put("uranium", "铀");
        result.put("osmium", "锇");
        result.put("netherite", "下界合金");
        result.put("naquadah", "硅岩");
        result.put("refined", "精制");
        result.put("glowstone", "荧石");
        result.put("alloy", "合金");
        result.put("utility", "实用");
        result.put("sodium", "钠");
        result.put("hydroxide", "氢氧化物");
        result.put("antimatter", "反物质");
        result.put("biomass", "生物质");
        result.put("singularity", "奇点");
        result.put("nether", "下界");
        result.put("star", "恒星");
        result.put("coal", "煤炭");
        result.put("elastic", "弹力");
        result.put("convergent", "收敛");
        result.put("infuse", "灌注");
        result.put("stardust", "星尘");
        result.put("starry", "星");
        result.put("sky", "空");
        result.put("vibration", "振荡");
        result.put("resonance", "共振");
        result.put("enhanced", "强化");
        result.put("illusion", "幻象");
        result.put("insert", "输入");
        result.put("xp", "经验");
        result.put("lava", "溶岩");
        result.put("netherrack", "下界岩");
        result.put("ammonia", "氨");
        result.put("water", "水");
        result.put("nitric", "硝");
        result.put("acid", "酸");
        result.put("aqua", "王");
        result.put("regia", "水");
        result.put("ether", "以太");
        result.put("oleum", "发烟硫酸");
        result.put("polonium", "钋");
        result.put("cobblestone", "圆石");
        result.put("water", "水");
        result.put("radioactive", "放射性");
        result.put("fertilizer", "肥料");
        result.put("air", "空气");
        result.put("wisdom", "知识");
        result.put("radiation", "辐射");
        // material states
        result.put("astral", "星界");
        result.put("ingot", "锭");
        result.put("block", "方块");
        result.put("processor", "处理器");
        result.put("press", "压印模板");
        result.put("dust", "粉");
        result.put("golden", "金的");
        result.put("charged", "充能");
        result.put("cluster", "簇");
        result.put("enriched", "富集");
        result.put("control", "控制电路");
        result.put("circuit", "电路板");
        result.put("supply", "供給");
        result.put("upgrade", "升级");
        result.put("starlight", "星辉");
        result.put("rivulet", "凝流");
        result.put("dirty", "污浊");
        result.put("shining", "闪耀");
        result.put("specific", "奇异");
        result.put("compressed", "压缩");
        result.put("clump", "碎块");
        result.put("crushed", "碎屑");
        result.put("crystal", "结晶");
        result.put("raw", "粗");
        result.put("shard", "碎片");
        result.put("mekanical", "通机");
        result.put("universal", "通用");
        result.put("gas", "气");
        result.put("infuse", "灌注");
        result.put("red", "红石");
        result.put("soul", "灵魂");
        result.put("mixed", "混合");
        result.put("slurry", "浆液");
        result.put("clean", "纯净");
        result.put("paste", "浆糊");
        result.put("containing", "含");
        result.put("sealing", "密封");
        result.put("intake", "吸入");
        // other
        result.put("item", "物品");
        result.put("spacetime", "时空");
        result.put("modulation", "调制");
        result.put("core", "核心");
        result.put("light", "光");
        result.put("cable", "通用线缆");
        result.put("bucket", "桶");
        result.put("ratio", "比例");
        result.put("evenly", "均等");
        result.put("inserter", "输入器");
        result.put("base", "基板");
        result.put("ame", "AME");
        return result;
    }).get();

}