//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.armament.mixin;


import net.goo.armament.util.helpers.EnvironmentColorManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Biome.class})
public abstract class BiomeMixin {
    @Final
    @Shadow
    private BiomeSpecialEffects specialEffects;

    public BiomeMixin() {
    }

    @Shadow
    protected abstract int getGrassColorFromTexture();

    @Shadow
    protected abstract int getFoliageColorFromTexture();

    @Overwrite
    public int getSkyColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.armament.util.helpers.EnvironmentColorManager.ColorType.SKY);
        return color == -1 ? this.specialEffects.getSkyColor() : color;
    }

    @Overwrite
    public int getWaterColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.armament.util.helpers.EnvironmentColorManager.ColorType.WATER);
        return color == -1 ? this.specialEffects.getWaterColor() : color;
    }

    @Overwrite
    public int getWaterFogColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.armament.util.helpers.EnvironmentColorManager.ColorType.WATER_FOG);
        return color == -1 ? this.specialEffects.getWaterFogColor() : color;
    }

    @Overwrite
    public int getFogColor() {
        int color = net.goo.armament.util.helpers.EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.FOG);
        return color == -1 ? this.specialEffects.getFogColor() : color;
    }

    @Overwrite
    public int getFoliageColor() {
        int color = net.goo.armament.util.helpers.EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.FOLIAGE);
        return color == -1 ? this.specialEffects.getFoliageColorOverride().orElseGet(this::getFoliageColorFromTexture) : color;
    }

    @Overwrite
    public int getGrassColor(double posX, double posY) {
        int color = EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.GRASS);
        return color == -1 ? this.specialEffects.getGrassColorModifier().modifyColor(posX, posY, this.specialEffects.getGrassColorOverride().orElseGet(this::getGrassColorFromTexture)) : color;
    }
}
