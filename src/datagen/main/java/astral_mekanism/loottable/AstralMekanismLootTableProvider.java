package astral_mekanism.loottable;

import java.util.List;
import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class AstralMekanismLootTableProvider extends LootTableProvider {

    public AstralMekanismLootTableProvider(PackOutput output) {
        super(output, Set.of(),
                List.of(new SubProviderEntry(
                        AstralMekanismBlockLoot::new,
                        LootContextParamSets.BLOCK)));
    }

}
