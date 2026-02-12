package net.goo.brutality.util;

import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

@OnlyIn(Dist.CLIENT)
public interface ClientModResources {

    Lazy<FX> CREASE_OF_CREATION_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "crease_of_creation_particle")));
    Lazy<FX> PHOTON_TRAIL_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "photon_trail")));
    Lazy<FX> VAMPIRE_TRIAL_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "vampire_trail")));
    Lazy<FX> CELESTIAL_STARBOARD_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "celestial_starboard_trail")));
    Lazy<FX> LIGHTNING_STRIKE_BURST_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_strike_burst")));
    Lazy<FX> LIGHTNING_AURA_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_aura")));
    Lazy<FX> LIGHTNING_TRAIL_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_trail")));
    Lazy<FX> GRAVITY_FIELD_UP_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_up")));
    Lazy<FX> GRAVITY_FIELD_DOWN_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_down")));
    Lazy<FX> RAINBOW_TRAIL_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "rainbow_trail")));
    Lazy<FX> ABYSS_TRAIL_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "abyss_trail")));
    Lazy<FX> RUINED_AURA_FX = Lazy.of(() -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "ruined_particle")));

    @OnlyIn(Dist.CLIENT)
    public static FX getCreaseOfCreationFX() {
        return getSafeFX(CREASE_OF_CREATION_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getPhotonTrailFX() {
        return getSafeFX(PHOTON_TRAIL_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getVampireTrialFX() {
        return getSafeFX(VAMPIRE_TRIAL_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getCelestialStarboardFX() {
        return getSafeFX(CELESTIAL_STARBOARD_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getLightningStrikeBurstFX() {
        return getSafeFX(LIGHTNING_STRIKE_BURST_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getLightningAuraFX() {
        return getSafeFX(LIGHTNING_AURA_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getLightningTrailFX() {
        return getSafeFX(LIGHTNING_TRAIL_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getGravityFieldUpFX() {
        return getSafeFX(GRAVITY_FIELD_UP_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getGravityFieldDownFX() {
        return getSafeFX(GRAVITY_FIELD_DOWN_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getRainbowTrailFX() {
        return getSafeFX(RAINBOW_TRAIL_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getAbyssTrailFX() {
        return getSafeFX(ABYSS_TRAIL_FX);
    }

    @OnlyIn(Dist.CLIENT)
    public static FX getRuinedAuraFX() {
        return getSafeFX(RUINED_AURA_FX);
    }
    @OnlyIn(Dist.CLIENT)
    private static FX getSafeFX(Lazy<FX> lazyFX) {
        try {
            FX fx = lazyFX.get();
            if (fx == null) {
                Brutality.LOGGER.warn("FX is null! This may be due to early loading (before particle registration)");
                return FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "empty_placeholder"));
            }
            return fx;
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to get FX", e);
            return FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "empty_placeholder"));
        }
    }
}

