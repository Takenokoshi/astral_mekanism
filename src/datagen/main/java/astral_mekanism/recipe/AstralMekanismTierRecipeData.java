package astral_mekanism.recipe;

import com.jerry.mekanism_extras.common.registry.ExtraItem;

import appeng.core.definitions.AEItems;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.registries.AstralMekanismItems;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import io.github.masyumero.emextras.common.registry.EMExtrasItem;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public enum AstralMekanismTierRecipeData {
    ESSENTIAL_TO_BASIC(
            AstralMekanismTier.ESSENTIAL,
            AstralMekanismTier.BASIC,
            MekanismItems.BRONZE_INGOT,
            AstralMekanismItems.CONVERGENT_ALLOY,
            AstralMekanismItems.RESONANCE_CONTROL_CIRCUIT,
            Items.IRON_INGOT,
            MekanismItems.BASIC_CONTROL_CIRCUIT),
    BASIC_TO_ADVANCED(
            AstralMekanismTier.BASIC,
            AstralMekanismTier.ADVANCED,
            EMItems.REFINED_REDSTONE_INGOT,
            MekanismItems.INFUSED_ALLOY,
            MekanismItems.ADVANCED_CONTROL_CIRCUIT),
    ADVANCED_TO_ELITE(
            AstralMekanismTier.ADVANCED,
            AstralMekanismTier.ELITE,
            Items.GOLD_INGOT,
            MekanismItems.REINFORCED_ALLOY,
            MekanismItems.ELITE_CONTROL_CIRCUIT),
    ELITE_TO_ULTIMATE(
            AstralMekanismTier.ELITE,
            AstralMekanismTier.ULTIMATE,
            AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
            AstralMekanismItems.ENCHANTED_ALLOY,
            AstralMekanismItems.ENHANCED_CONTROL_CIRCUIT,
            MekanismItems.ATOMIC_ALLOY,
            MekanismItems.ULTIMATE_CONTROL_CIRCUIT),
    ULTIMATE_TO_ABSOLUTE(
            AstralMekanismTier.ULTIMATE,
            AstralMekanismTier.ABSOLUTE,
            Items.GLOWSTONE,
            EMItems.HYPERCHARGED_ALLOY,
            ExtraItem.RADIANCE_ALLOY,
            EMExtrasItem.ABSOLUTE_OVERCLOCKED_CONTROL_CIRCUIT),
    ABSOLUTE_TO_SUPREME(
            AstralMekanismTier.ABSOLUTE,
            AstralMekanismTier.SUPREME,
            Items.SHULKER_SHELL,
            EMItems.SUBATOMIC_ALLOY,
            ExtraItem.THERMONUCLEAR_ALLOY,
            EMExtrasItem.SUPREME_QUANTUM_CONTROL_CIRCUIT),
    SUPREME_TO_COSMIC(
            AstralMekanismTier.SUPREME,
            AstralMekanismTier.COSMIC,
            MekanismItems.REFINED_OBSIDIAN_INGOT,
            EMItems.SINGULAR_ALLOY,
            ExtraItem.SHINING_ALLOY,
            EMExtrasItem.COSMIC_DENSE_CONTROL_CIRCUIT),
    COSMIC_TO_INFINITE(
            AstralMekanismTier.COSMIC,
            AstralMekanismTier.INFINITE,
            Items.NETHERITE_INGOT,
            EMItems.EXOVERSAL_ALLOY,
            ExtraItem.SPECTRUM_ALLOY,
            EMExtrasItem.INFINITE_MULTIVERSAL_CONTROL_CIRCUIT),
    INFINITE_TO_ASTRAL(
            AstralMekanismTier.INFINITE,
            AstralMekanismTier.ASTRAL,
            MekanismItems.POLONIUM_PELLET,
            AstralMekanismItems.STARDUST_ALLOY,
            AstralMekanismItems.ILLUSION_CONTROL_CIRCUIT),
            ;

    private AstralMekanismTierRecipeData(
            AstralMekanismTier beforeTier,
            AstralMekanismTier afterTier,
            ItemLike centerItem,
            ItemLike leftAlloy,
            ItemLike leftCircuit,
            ItemLike rightAlloy,
            ItemLike rightCircuit) {
        this.beforeTier = beforeTier;
        this.afterTier = afterTier;
        this.centerItem = centerItem;
        this.leftAlloy = leftAlloy;
        this.leftCircuit = leftCircuit;
        this.rightAlloy = rightAlloy;
        this.rightCircuit = rightCircuit;
    }

    private AstralMekanismTierRecipeData(
            AstralMekanismTier beforeTier,
            AstralMekanismTier afterTier,
            ItemLike centerItem,
            ItemLike alloy,
            ItemLike circuit) {
        this(beforeTier, afterTier, centerItem, alloy, circuit, alloy, circuit);
    }

    private AstralMekanismTierRecipeData(
            AstralMekanismTier beforeTier,
            AstralMekanismTier afterTier,
            ItemLike centerItem,
            ItemLike leftAlloy,
            ItemLike rightAlloy,
            ItemLike circuit) {
        this(beforeTier, afterTier, centerItem, leftAlloy, circuit, rightAlloy, circuit);
    }

    public final AstralMekanismTier beforeTier;
    public final AstralMekanismTier afterTier;
    public final ItemLike centerItem;
    public final ItemLike leftAlloy;
    public final ItemLike leftCircuit;
    public final ItemLike rightAlloy;
    public final ItemLike rightCircuit;
}
