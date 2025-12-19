package astral_mekanism.registries;

import java.util.Map;
import java.util.Set;
import astral_mekanism.AstralMekanismConfig;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPrecisionSawmill;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSPS;
import astral_mekanism.block.blockentity.compact.BECompactFIR;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.compact.BECompactTEP;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
import astral_mekanism.block.blockentity.generator.BEGasBurningGenerator;
import astral_mekanism.block.blockentity.generator.BEHeatGenerator;
import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalInscriber;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalPresser;
import astral_mekanism.block.blockentity.normalmachine.BEMelter;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.shape.AMBlockShapes;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.BlockTypeTileUtils;
import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import mekanism.api.math.FloatingLong;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.generators.common.GeneratorsLang;

public class AstralMekanismBlockTypes {
    private AstralMekanismBlockTypes() {
    }

    public static final BlockTypeMachine<BEAstralSPS> ASTRAL_SPS = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_SPS.getTileRO(), MekanismLang.DESCRIPTION_SPS_CASING)
            .withGui(() -> AstralMekanismMachines.ASTRAL_SPS.getContainerRO())
            .withEnergyConfig(() -> FloatingLong.create(1000000000), () -> FloatingLong.MAX_VALUE)
            .withSupportedUpgrades(Set.of(Upgrade.MUFFLING))
            .build();

    public static final BlockTypeMachine<BEAstralPrecisionSawmill> ASTRAL_PRECISION_SAWMILL = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL.getTileRO(),
                    MekanismLang.DESCRIPTION_PRECISION_SAWMILL)
            .withGui(() -> AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL.getContainerRO())
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, () -> FloatingLong.MAX_VALUE)
            .withCustomShape(AMBlockShapes.ASTRAL_MACHINE_BASIC)
            .build();

    public static final Map<AstralMekGeneratorTier, BlockTypeTile<BEGasBurningGenerator>> GAS_BURNING_GENERATORS = BlockTypeTileUtils
            .buildMap(AstralMekGeneratorTier.class,
                    t -> () -> AstralMekanismMachines.GAS_BURNING_GENERATORS.get(t).getTileRO(),
                    GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR,
                    t -> () -> AstralMekanismMachines.GAS_BURNING_GENERATORS.get(t).getContainerRO(),
                    (t, b) -> b.with(new AttributeTier<AstralMekGeneratorTier>(t))
                            .withCustomShape(AMBlockShapes.GAS_BURNING_GENERATOR));

    public static final Map<AstralMekGeneratorTier, BlockTypeTile<BEHeatGenerator>> HEAT_GENERATORS = BlockTypeTileUtils
            .buildMap(AstralMekGeneratorTier.class,
                    t -> () -> AstralMekanismMachines.HEAT_GENERATORS.get(t).getTileRO(),
                    GeneratorsLang.DESCRIPTION_HEAT_GENERATOR,
                    t -> () -> AstralMekanismMachines.HEAT_GENERATORS.get(t).getContainerRO(),
                    (t, b) -> b.with(new AttributeTier<AstralMekGeneratorTier>(t))
                            .withCustomShape(AMBlockShapes.HEAT_GENERATOR));

    public static final BlockTypeTile<BEUniversalStorage> UNIVERSAL_STORAGE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.UNIVERSAL_STORAGE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE)
            .withGui(() -> AstralMekanismMachines.UNIVERSAL_STORAGE.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEItemSortableStorage> ITEM_SORTABLE_STORAGE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.ITEM_SORTABLE_STORAGE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE)
            .withGui(() -> AstralMekanismMachines.ITEM_SORTABLE_STORAGE.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEGlowstoneNeutronActivator> GLOWSTONE_NEUTRON_ACTIVATOR = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR)
            .withGui(() -> AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR.getContainerRO())
            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR)
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEMelter> MELTER = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.MELTER.getTileRO(), AstralMekanismLang.DESCRIPTION_MELTER)
            .withGui(() -> AstralMekanismMachines.MELTER.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BECompactTEP> COMPACT_TEP = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.COMPACT_TEP.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_COMPACT_TEP)
            .withGui(() -> AstralMekanismMachines.COMPACT_TEP.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BEFluidInfuser> FLUID_INFUSER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.FLUID_INFUSER.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_FLUID_INFUSER)
            .withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(10000000))
            .withGui(() -> AstralMekanismMachines.FLUID_INFUSER.getContainerRO())
            .withCustomShape(AMBlockShapes.FLUID_INFUSER)
            .build();

    public static final BlockTypeMachine<BEMekanicalCharger> MEKANICAL_CHARGER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.MEKANICAL_CHARGER.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER)
            .withEnergyConfig(() -> FloatingLong.create(4 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(100000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.MEKANICAL_CHARGER.getContainerRO())
            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER)
            .build();

    public static final BlockTypeMachine<BEMekanicalInscriber> MEKANICAL_INSCRIBER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.MEKANICAL_INSCRIBER.getTileRO(), null)
            .withEnergyConfig(() -> FloatingLong.create(4 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(100000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.MEKANICAL_INSCRIBER.getContainerRO())
            .build();

    public static final BlockTypeMachine<BEMekanicalPresser> MEKANICAL_PRESSER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.MEKANICAL_PRESSER.getTileRO(), null)
            .withEnergyConfig(() -> FloatingLong.create(4 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(100000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.MEKANICAL_PRESSER.getContainerRO())
            .build();

    public static final BlockTypeMachine<BEAstralCrafter> ASTRAL_CRAFTER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_CRAFTER.getTileRO(), AstralMekanismLang.ITEM_GROUP)
            .withEnergyConfig(() -> FloatingLong.create(400 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(10000000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.ASTRAL_CRAFTER.getContainerRO())
            .build();

    public static final BlockTypeTile<BECompactFIR> COMPACT_FIR = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.COMPACT_FIR.getTileRO(),
                    GeneratorsLang.DESCRIPTION_FISSION_REACTOR_CASING)
            .withGui(() -> AstralMekanismMachines.COMPACT_FIR.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BECompactSPS> COMPACT_SPS = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.COMPACT_SPS.getTileRO(), MekanismLang.DESCRIPTION_SPS_CASING)
            .withGui(() -> AstralMekanismMachines.COMPACT_SPS.getContainerRO())
            .withEnergyConfig(() -> FloatingLong.create(1000000000), () -> FloatingLong.create(10000000000l))
            .withSupportedUpgrades(Set.of(Upgrade.MUFFLING))
            .build();
}
