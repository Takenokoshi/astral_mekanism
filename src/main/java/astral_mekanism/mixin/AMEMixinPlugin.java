package astral_mekanism.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.minecraftforge.fml.loading.LoadingModList;

public class AMEMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains(".mekmm.")) {
            return LoadingModList.get().getModFileById("mekmm") != null;
        }
        if (mixinClassName.contains(".morethermalevaporation.")) {
            return LoadingModList.get().getModFileById("morethermalevaporation") != null;
        }
        if (mixinClassName.contains(".morethermalevaporationcompat.")) {
            return LoadingModList.get().getModFileById("morethermalevaporationcompat") != null;
        }
        if (mixinClassName.contains(".BasicInventorySlotMixin")) {
            return LoadingModList.get().getModFileById("biggerstacks") != null;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

}
