package net.goo.brutality.util;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomDeathMessageDamageSource extends DamageSource {
    protected final int variantCount;

    public RandomDeathMessageDamageSource(Holder<DamageType> pType, @Nullable Entity pDirectEntity, @Nullable Entity pCausingEntity, @Nullable Vec3 pDamageSourcePosition, int variantCount) {
        super(pType, pDirectEntity, pCausingEntity, pDamageSourcePosition);
        this.variantCount = variantCount;
    }

    public RandomDeathMessageDamageSource(Holder<DamageType> pType, @Nullable Entity pDirectEntity, @Nullable Entity pCausingEntity, int variantCount) {
        super(pType, pDirectEntity, pCausingEntity);
        this.variantCount = variantCount;
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity pLivingEntity) {
        Entity causingEntity = getEntity();

        int variant = pLivingEntity.getRandom().nextIntBetweenInclusive(0, variantCount);

        String deathMsg = "death.attack." + this.type().msgId() + "." + variant;
        if (causingEntity == null) {
            if (pLivingEntity.getKillCredit() != null) {
                causingEntity = pLivingEntity.getKillCredit();
            }
        }

        if (causingEntity != null) {
            String deathMsgPlayer = deathMsg + ".player";
            return Component.translatable(deathMsgPlayer, pLivingEntity.getDisplayName(), causingEntity.getDisplayName());
        } else {
            return Component.translatable(deathMsg, pLivingEntity.getDisplayName());
        }
    }
}
