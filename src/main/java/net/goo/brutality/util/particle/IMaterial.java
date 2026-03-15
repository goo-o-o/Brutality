package net.goo.brutality.util.particle;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KilaBash
 * @date 2023/5/29
 * @implNote Material
 */
@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public interface IMaterial {

    List<Class<? extends IMaterial>> MATERIALS = new ArrayList<>(List.of(
            TextureMaterial.class, CustomShaderMaterial.class, BlockTextureSheetMaterial.class
    ));


    void begin(boolean isInstancing);

    void end(boolean isInstancing);


}
