package astral_mekanism.mixin.mekanism;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;

@Mixin(value = TileEntityMetallurgicInfuser.class, remap = false)
public abstract class TileEntityMetallurgicInfuserMixin {

    @Redirect(
        method = "getInitialInfusionTanks",
        at = @At(
            value = "INVOKE",
            target = "Lmekanism.api.chemical.ChemicalTankBuilder;create(JLjava/util/function/BiPredicate;Ljava/util/function/BiPredicate;Ljava.util.function.Predicate;Lmekanism.api.IContentsListener;)Lmekanism/common/capabilities/chemical/infuse/InfusionTank;"
        )
    )
    private IInfusionTank astral_mekanism$redirectCreate(
        ChemicalTankBuilder<InfuseType, InfusionStack, IInfusionTank> self,
        long capacity,
        BiPredicate<InfuseType, AutomationType> alwaysTrue,
        BiPredicate<InfuseType, AutomationType> canInsert,
        Predicate<InfuseType> canExtract,
        IContentsListener listener
    ) {
        return self.create(
            1_000_000L,
            alwaysTrue,
            canInsert,
            canExtract,
            listener
        );
    }
}
