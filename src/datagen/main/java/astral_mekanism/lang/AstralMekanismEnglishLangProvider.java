package astral_mekanism.lang;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismFluids;
import astral_mekanism.registries.AstralMekanismGases;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismSlurries;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IItemProvider;
import mekanism.api.text.ILangEntry;
import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.common.registration.impl.GasRegistryObject;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;
import mekanism.common.registration.impl.SlurryRegistryObject;
import mekanism.common.util.text.InputValidator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class AstralMekanismEnglishLangProvider extends LanguageProvider {

    public AstralMekanismEnglishLangProvider(PackOutput output) {
        super(output, AstralMekanismID.MODID, "en_us_generated");
    }

    @Override
    protected void addTranslations() {
        for (MachineRegistryObject<?, ?, ?, ?> machine : AstralMekanismMachines.MACHINES.getAllMachines()) {
            addMachine(machine);
        }
        for (IBlockProvider block : AstralMekanismBlocks.BLOCKS.getAllBlocks()) {
            addBlock(block);
        }
        for (IItemProvider item : AstralMekanismItems.ITEMS.getAllItems()) {
            addItem(item);
        }
        for (FluidRegistryObject<?, ?, ?, ?, ?> fluid : AstralMekanismFluids.FLUIDS.getAllFluids()) {
            addFluid(fluid);
        }
        for (GasRegistryObject<?> gas : AstralMekanismGases.GASES.getAllGases()) {
            addGas(gas);
        }
        for (InfuseTypeRegistryObject<?> infuseType : AstralMekanismInfuseTypes.INFUSE_TYPES.getAllInfuseType()) {
            addInfuse(infuseType);
        }
        for (SlurryRegistryObject<?, ?> slurry : AstralMekanismSlurries.SLURRIES.getAllSlurries()) {
            addSlurry(slurry);
        }
    }

    private void addMachine(MachineRegistryObject<?, ?, ?, ?> machine) {
        addBlock(machine);
        add("container.astral_mekanism." + machine.getRegistryName().getPath(),
                toTitle(machine.getRegistryName().getPath()));
    }

    private void addBlock(IBlockProvider block) {
        add(block.getBlock(), toTitle(block.getRegistryName().getPath()));
    }

    private void addItem(IItemProvider item) {
        add(item.asItem(), toTitle(item.getRegistryName().getPath()));
    }

    private void addLangEntry(ILangEntry entry) {
        String[] parts = entry.getTranslationKey().split("\\.");
        add(entry.getTranslationKey(), toTitle(parts[parts.length - 1]));
    }

    private void addFluid(FluidRegistryObject<?, ?, ?, ?, ?> fluid) {
        String path = fluid.getRegistryName().getPath();
        add(fluid.getBucket(), toTitle(path + "_bucket"));
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
        add(slurry.getCleanSlurry().getTranslationKey(), toTitle(slurry.getCleanSlurry().getRegistryName().getPath()));
        add(slurry.getDirtySlurry().getTranslationKey(), toTitle(slurry.getDirtySlurry().getRegistryName().getPath()));
    }

    private static String toTitle(String path) {
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();

        if (parts[0].equals("crystal") && parts[parts.length - 1].equals("charged")) {
            parts[0] = "charged";
            parts[parts.length - 1] = "crystal";
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
            if (parts[i].equals("tep")) {
                sb.append("Thermal Evaporation Plant");
            } else if (parts[i].equals("fir")) {
                sb.append("Fission Reactor");
            } else if (parts[i].equals("sps")) {
                sb.append("SPS");
            } else if (parts[i].equals("prc")) {
                sb.append("Pressurized REaction Chamber");
            } else if (parts[i].equals("gna")) {
                sb.append("Glowstone Neutron Activator");
            } else if (InputValidator.DIGIT.test(parts[i].charAt(0))) {
                sb.append(parts[i]);
            } else {
                sb.append(Character.toUpperCase(parts[i].charAt(0)));
                if (parts[i].length() > 1) {
                    sb.append(parts[i].substring(1));
                }
            }
            if (i < parts.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
