package net.goo.brutality.util;


import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) // 核心：标记为仅客户端
public interface ClientModResources{
    // 所有FX相关常量移到这里，仅客户端初始化
    FX CREASE_OF_CREATION_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "crease_of_creation_particle"));
    FX PHOTON_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "photon_trail"));
    FX VAMPIRE_TRIAL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "vampire_trail"));
    FX CELESTIAL_STARBOARD_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "celestial_starboard_trail"));
    FX LIGHTNING_STRIKE_BURST_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_strike_burst"));
    FX LIGHTNING_AURA_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_aura"));
    FX LIGHTNING_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_trail"));
    FX GRAVITY_FIELD_UP_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_up"));
    FX GRAVITY_FIELD_DOWN_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_down"));
    FX RAINBOW_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "rainbow_trail"));
    FX ABYSS_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "abyss_trail"));
    FX RUINED_AURA_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "ruined_particle"));
}