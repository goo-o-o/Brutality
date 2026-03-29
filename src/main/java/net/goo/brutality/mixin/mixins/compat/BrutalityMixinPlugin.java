package net.goo.brutality.mixin.mixins.compat;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class BrutalityMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("TooltipRendererMixinTooltipOverhaul")) {
            return isLoaded("tooltipoverhaul");
        }
        if (mixinClassName.contains("net.goo.brutality.mixin.mixins.sodium")) {
            return isLoaded("sodium");
        }


//        if (mixinClassName.contains("net.goo.brutality.mixin.mixins.no_iris")) {
//            return !(isLoaded("iris") || isLoaded("oculus"));
//        }
        return true;
    }

    private boolean isLoaded(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    // Leave other methods empty/default
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}