package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.mixin_helpers.IBrutalityAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AttributeMap.class)
public abstract class AttributeMapMixin implements IBrutalityAttribute {

    @Shadow @Nullable public abstract AttributeInstance getInstance(Attribute pAttribute);

    @Unique
    private LivingEntity brutality$owner;

    @Override
    public void brutality$setOwner(LivingEntity entity) {
        this.brutality$owner = entity;
    }

    @Inject(method = "getInstance*", at = @At("RETURN"))
    private void brutality$tagNewInstance(Attribute pAttribute, CallbackInfoReturnable<AttributeInstance> cir) {
        AttributeInstance instance = cir.getReturnValue();
        if (instance != null && this.brutality$owner != null) {
            ((IBrutalityAttribute)instance).brutality$setOwner(this.brutality$owner);
        }
    }

}