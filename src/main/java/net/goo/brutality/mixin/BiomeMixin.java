//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.mixin;


import net.goo.brutality.util.helpers.EnvironmentColorManager;
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

    /**
     * @author goo
     * @reason changes sky color
     */
    @Overwrite
    public int getSkyColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.brutality.util.helpers.EnvironmentColorManager.ColorType.SKY);
        return color == -1 ? this.specialEffects.getSkyColor() : color;
    }

    /**
     * @author goo
     * @reason changes water color
     */
    @Overwrite
    public int getWaterColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.brutality.util.helpers.EnvironmentColorManager.ColorType.WATER);
        return color == -1 ? this.specialEffects.getWaterColor() : color;
    }

    /**
     * @author goo
     * @reason changes water fog color
     */
    @Overwrite
    public int getWaterFogColor() {
        int color = EnvironmentColorManager.getColorOverride(net.goo.brutality.util.helpers.EnvironmentColorManager.ColorType.WATER_FOG);
        return color == -1 ? this.specialEffects.getWaterFogColor() : color;
    }

    /**
     * @author goo
     * @reason changes fog color
     */
    @Overwrite
    public int getFogColor() {
        int color = net.goo.brutality.util.helpers.EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.FOG);
        return color == -1 ? this.specialEffects.getFogColor() : color;
    }

    /**
     * @author goo
     * @reason changes foliage color
     */
    @Overwrite
    public int getFoliageColor() {
        int color = net.goo.brutality.util.helpers.EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.FOLIAGE);
        return color == -1 ? this.specialEffects.getFoliageColorOverride().orElseGet(this::getFoliageColorFromTexture) : color;
    }

    /**
     * @author goo
     * @reason changes grass color
     */
    @Overwrite
    public int getGrassColor(double posX, double posY) {
        int color = EnvironmentColorManager.getColorOverride(EnvironmentColorManager.ColorType.GRASS);
        return color == -1 ? this.specialEffects.getGrassColorModifier().modifyColor(posX, posY, this.specialEffects.getGrassColorOverride().orElseGet(this::getGrassColorFromTexture)) : color;
    }
}
